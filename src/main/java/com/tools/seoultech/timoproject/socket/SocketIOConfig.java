package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {

    @Value("${socket-server.port}")
    private int port;
    @Value("${socket-server.host}")
    private String host;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);

        // CORS 등 세부 설정
        // JWT
        return new SocketIOServer(config);
    }

    @Bean
    public WebSocketAddMappingSupporter webSocketAddMappingSupporter(ConfigurableListableBeanFactory beanFactory) {
        return new WebSocketAddMappingSupporter(beanFactory);
    }

    // SocketRunner에서 server.start() 호출
}

