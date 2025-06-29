package com.tools.seoultech.timoproject.notification.channel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationChannelType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.notification.dto.NotificationResponse;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SseNotificationChannel implements NotificationChannel {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.SSE;
    }

    @Override
    public void send(NotificationChannelRequest request) {
        SseEmitter emitter = emitters.get(request.memberId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name(request.type().name())
                        .data(createNotificationResponse(request)));
            } catch (IOException e) {
                log.warn("SSE 전송 실패 - memberId: {}", request.memberId());
                emitters.remove(request.memberId());
            }
        }
    }

    @Override
    public boolean isEnabled(Member member, NotificationType type) {
        return true; // SSE는 항상 활성화
    }

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onError((e) -> emitters.remove(memberId));

        return emitter;
    }

    private NotificationResponse createNotificationResponse(NotificationChannelRequest request) {
        return new NotificationResponse(
                null,
                request.type(),
                request.message(),
                request.redirectUrl(),
                false,
                LocalDateTime.now()
        );
    }
}
