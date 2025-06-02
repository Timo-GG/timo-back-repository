package com.tools.seoultech.timoproject.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

	private final NotificationService notificationService;
	private final JwtResolver jwtResolver;

	/**
	 * SSE 구독 (클라이언트가 알림 스트림 연결)
	 * 토큰을 쿼리 파라미터로 받아서 memberId 추출
	 */
	@Operation(summary = "알림 SSE 구독", description = "클라이언트에서 SSE로 알림 스트림을 구독합니다. 쿼리 파라미터로 받은 토큰에서 memberId를 추출합니다.")
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	public SseEmitter subscribe(@RequestParam String token) {
		Long memberId = jwtResolver.getMemberIdFromAccessToken(token);
		return notificationService.subscribe(memberId);
	}

	/**
	 * 특정 사용자에게 알림 전송
	 */
	@Operation(summary = "[알림 전송", description = "특정 사용자에게 알림을 전송합니다.")
	@PostMapping("/send")
	public ResponseEntity<Void> sendNotification(@CurrentMemberId Long memberId,
												 @RequestBody NotificationRequest request) {
		notificationService.sendNotification(memberId, request);
		return ResponseEntity.ok().build();
	}

	/**
	 * 특정 유저의 안 읽은 알림 조회
	 */
	@Operation(summary = "안 읽은 알림 조회", description = "특정 사용자의 안 읽은 알림 목록을 조회합니다.")
	@GetMapping("/unread")
	public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@CurrentMemberId Long memberId) {
		List<NotificationResponse> response = notificationService.getUnreadNotifications(memberId).stream()
				.map(NotificationResponse::from)
				.toList();

		return ResponseEntity.ok(response);
	}

	/**
	 * 특정 알림 읽음 처리
	 */
	@Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 처리합니다.")
	@PostMapping("/{notificationId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
		notificationService.markAsRead(notificationId);
		return ResponseEntity.ok().build();
	}
}
