package com.tools.seoultech.timoproject.chat.socket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("socket")
public class SocketProperty {

      private final String getMessageEvent = "get_message";
      private Integer port;
      private String memberKey;
      private String roomKey;
}
