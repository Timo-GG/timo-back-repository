package com.tools.seoultech.timoproject.notification.channel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tools.seoultech.timoproject.notification.enumType.NotificationChannelType;
import org.springframework.stereotype.Component;

@Component
public class NotificationChannelFactory {
    private final Map<NotificationChannelType, NotificationChannel> channels;

    public NotificationChannelFactory(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::getChannelType,
                        Function.identity()
                ));
    }

    public NotificationChannel getChannel(NotificationChannelType type) {
        return channels.get(type);
    }

    public List<NotificationChannel> getAllChannels() {
        return List.copyOf(channels.values());
    }

    public boolean isChannelAvailable(NotificationChannelType type) {
        return channels.containsKey(type);
    }
}
