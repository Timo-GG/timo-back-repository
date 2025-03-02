package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class WebSocketAddMappingSupporter {

    private final ConfigurableListableBeanFactory beanFactory;
    private SocketIOServer socketIOServer;

    public void addListeners(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;

        final List<Class<?>> classes = beanFactory.getBeansWithAnnotation(SocketController.class).values()
                .stream().map(Object::getClass)
                .collect(Collectors.toList());

        for (Class<?> cls : classes) {
            List<Method> methods = findSocketMappingAnnotatedMethods(cls);
            addSocketServerEventListener(cls, methods);
        }

    }

    private void addSocketServerEventListener(Class<?> controller, List<Method> methods) {
        for (Method method : methods) {
            SocketMapping socketMapping = method.getAnnotation(SocketMapping.class);
            String endpoint = socketMapping.endpoint();
            Class<?> dtoClass = socketMapping.requestCls();

            // netty-socketio에 이벤트 리스너 등록
            socketIOServer.addEventListener(endpoint, dtoClass, ((client, data, ackSender) -> {
                // @SocketMapping 메서드의 파라미터를 확인하여 필요한 인자를 준비
                List<Object> args = new ArrayList<>();
                for (Class<?> params : method.getParameterTypes()) {                        // Controller 메소드의 파라미터들
                    if (params.equals(SocketIOServer.class)) args.add(socketIOServer);      // SocketIOServer 면 주입
                    else if (params.equals(SocketIOClient.class)) args.add(client);         // 마찬가지
                    else if (params.equals(dtoClass)) args.add(data);
                }

                // 실제 컨트롤러의 메서드를 리플렉션으로 호출
                method.invoke(beanFactory.getBean(controller), args.toArray());
            }));
        }
    }

    private List<Method> findSocketMappingAnnotatedMethods(Class<?> cls) {
        return Arrays.stream(cls.getMethods())
                .filter(method -> method.getAnnotation(SocketMapping.class) != null)
                .collect(Collectors.toList());
    }

}
