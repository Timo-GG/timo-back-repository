package com.tools.seoultech.timoproject.notification.channel;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationChannelType;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;

public interface NotificationChannel {
    NotificationChannelType getChannelType();
    void send(NotificationChannelRequest request);
    boolean isEnabled(Member member, NotificationType type);
}