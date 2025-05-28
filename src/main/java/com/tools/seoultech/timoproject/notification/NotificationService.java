package com.tools.seoultech.timoproject.notification;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tools.seoultech.timoproject.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberService memberService;

	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	public SseEmitter subscribe(Long memberId) {
		return setupEmitter(memberId);
	}

	public void sendNotification(Long memberId, NotificationRequest request) {
		Member member = memberService.getById(memberId);

		String formattedMessage = request.getFormattedMessage();

		Notification notification = Notification.builder()
			.member(member)
			.type(request.type())
			.message(formattedMessage)
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
		Member member = memberService.getById(memberId);
		return notificationRepository.findByMemberAndIsReadFalse(member);
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
