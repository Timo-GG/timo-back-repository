package com.tools.seoultech.timoproject.notification;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.memberAccount.service.MemberAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberAccountService memberAccountService;

	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	public SseEmitter subscribe(Long memberId) {
		return setupEmitter(memberId);
	}

	public void sendNotification(Long memberId, NotificationRequest request) {
		MemberAccount memberAccount = memberAccountService.getById(memberId);

		Notification notification = Notification.builder()
			.memberAccount(memberAccount)
			.type(request.type())
			.redirectUrl(request.redirectUrl())
			.isRead(false)
			.build();

		notificationRepository.save(notification);

		SseEmitter emitter = emitters.get(memberId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event()
					.name(notification.getType().name())  // 이벤트 이름: DUO_ACCEPTED 같은 enum 이름
					.data(NotificationResponse.from(notification)));
			} catch (IOException e) {
				emitters.remove(memberId);
			}
		}
	}

	public List<Notification> getUnreadNotifications(Long memberId) {
		MemberAccount memberAccount = memberAccountService.getById(memberId);
		return notificationRepository.findByMemberAccountAndIsReadFalse(memberAccount);
	}

	public void markAsRead(Long notificationId) {
		notificationRepository.findById(notificationId).ifPresent(notification -> {
			notification.markAsRead();
			notificationRepository.save(notification);
		});
	}

	private SseEmitter setupEmitter(Long memberId) {
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		emitters.put(memberId, emitter);
		emitter.onCompletion(() -> emitters.remove(memberId));
		emitter.onTimeout(() -> emitters.remove(memberId));
		emitter.onError((e) -> emitters.remove(memberId));

		return emitter;
	}
}
