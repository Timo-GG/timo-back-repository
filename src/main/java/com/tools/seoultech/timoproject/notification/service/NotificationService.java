package com.tools.seoultech.timoproject.notification.service;

import java.util.List;

import com.tools.seoultech.timoproject.notification.Notification;
import com.tools.seoultech.timoproject.notification.NotificationRepository;
import com.tools.seoultech.timoproject.notification.dto.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.notification.channel.NotificationChannel;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.channel.SseNotificationChannel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final MemberService memberService;
	private final List<NotificationChannel> channels;
	private final SseNotificationChannel sseChannel;

	public SseEmitter subscribe(Long memberId) {
		return sseChannel.subscribe(memberId);
	}

	public void sendNotification(Long memberId, NotificationRequest request) {
		Member member = memberService.getById(memberId);
		String formattedMessage = request.getFormattedMessage();

		Notification notification = Notification.create(member, request, formattedMessage);
		notificationRepository.save(notification);

		NotificationChannelRequest channelRequest =
				NotificationChannelRequest.from(member, request, formattedMessage);

		channels.forEach(channel -> {
			if (channel.isEnabled(member, request.type())) {
				try {
					channel.send(channelRequest);
					log.debug("알림 전송 성공 - channel: {}, memberId: {}",
							channel.getChannelType(), memberId);
				} catch (Exception e) {
					log.error("알림 전송 실패 - channel: {}, memberId: {}",
							channel.getChannelType(), memberId, e);
				}
			}
		});
	}

	public List<Notification> getUnreadNotifications(Long memberId) {
		Member member = memberService.getById(memberId);
		return notificationRepository.findByMemberAndIsReadFalse(member);
	}

	public void markAsRead(Long notificationId) {
		notificationRepository.deleteById(notificationId);
	}
}
