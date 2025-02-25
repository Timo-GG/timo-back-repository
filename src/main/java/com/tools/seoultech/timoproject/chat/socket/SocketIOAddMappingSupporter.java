package com.tools.seoultech.timoproject.chat.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.chat.dto.MessageResponse;
import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tools.seoultech.timoproject.chat.model.MessageType.TEXT;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIOAddMappingSupporter {

    private final ConfigurableListableBeanFactory beanFactory;
    private final SocketProperty socketProperty;
    private final SocketService socketService;
    private final ObjectMapper objectMapper;
    private SocketIOServer socketIOServer;

    public void addListeners(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        final List<Class<?>> classes = beanFactory.getBeansWithAnnotation(SocketController.class)
                .values()
                .stream()
                .map(Object::getClass)
                .collect(toList());
        classes.forEach(cls -> {
            List<Method> methods = findSocketMappingAnnotatedMethods(cls);
            addSocketServerEventListener(cls, methods);
        });
    }

    private void addSocketServerEventListener(Class<?> controller, List<Method> methods) {
        SocketController socketController = controller.getAnnotation(SocketController.class);
        String eventValue = socketController.value();
        methods.forEach(method -> {
            SocketMapping socketMapping = method.getAnnotation(SocketMapping.class);
            String eventName = getEventName(socketMapping, eventValue); // value가 무엇인지 확인
            Class<?> dtoClass = socketMapping.requestCls();
            socketIOServer.addEventListener(eventName, dtoClass, (client, data, ackSender) -> {
                try {
                    List<Object> args = new ArrayList<>();
                    for (Class<?> params : method.getParameterTypes()) {
                        if (params.equals(SocketIOServer.class)) {
                            args.add(socketIOServer);
                        } else if (params.equals(SocketIOClient.class)) {
                            args.add(client);
                        } else if (params.equals(dtoClass)) {
                            args.add(data);
                        } else {
                            throw new RuntimeException("Invalid parameter type");
                        }
                    }
                    Object returnObject = method.invoke(beanFactory.getBean(controller), args.toArray());
                    if (returnObject != null) {
                        try{
                            MessageResponse messageResponse = (MessageResponse) returnObject;
                            switch (messageResponse.messageType()) {
                                case TEXT:
                                    socketService.sendMessage(
                                            client,
                                            messageResponse);
                            }
                        } catch (ClassCastException e) {
                            log.error("ClassCastException : {}", e.getMessage(), e);

                            ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
                            String errorMessage = errorCode.getMessage(e);
                            APIErrorResponse errorResponse = APIErrorResponse.of(false, errorCode.getCode(), errorMessage);

                            client.sendEvent(socketProperty.getGetMessageEvent(), errorResponse);
                        }
                    }
                }  catch (Exception e) {
                    log.error("Exception : {}", e.getMessage(), e);
                    exceptionHandle(e, client);
                }
            });
        });
    }

    private static String getEventName(SocketMapping socketMapping, String eventValue) {
        String endpoint = socketMapping.endpoint();
        return new StringBuilder()
                .append(eventValue)
                .append(endpoint)
                .toString();
    }

    private List<Method> findSocketMappingAnnotatedMethods(Class<?> cls) {
        return Arrays.stream(cls.getMethods())
                .filter(method -> method.getAnnotation(SocketMapping.class) != null)
                .collect(toList());
    }


    private void exceptionHandle(Exception e, SocketIOClient client) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        String errorMessage = errorCode.getMessage(e);
        APIErrorResponse errorResponse = APIErrorResponse.of(false, errorCode.getCode(), errorMessage);

        client.sendEvent(socketProperty.getGetMessageEvent(), errorResponse);
    }
}
