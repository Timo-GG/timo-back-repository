package com.tools.seoultech.timoproject.chat.socket.impl;


import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.chat.dto.MessageResponse;
import com.tools.seoultech.timoproject.chat.model.Message;
import com.tools.seoultech.timoproject.chat.model.MessageType;
import com.tools.seoultech.timoproject.chat.service.MessageService;
import com.tools.seoultech.timoproject.chat.socket.SocketProperty;
import com.tools.seoultech.timoproject.chat.socket.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketServiceImpl implements SocketService {

    private final MessageService messageService;
    private final SocketProperty socketProperty;
    private final Map<Long, Set<SocketIOClient>> clientsMap = new HashMap<>();
    private final ObjectMapper objectMapper;

    public void sendSocketMessage(SocketIOClient senderClient, Message message, String room) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("read_message", message);
            }
        }
    }

    @Transactional
    public void sendMessage(
            SocketIOClient senderClient,
            MessageResponse message) {
        var roomId = String.valueOf(getRoomId(senderClient));
        if (!clientsMap.containsKey(roomId)) {
            log.warn("Room not found. roomId: {}", roomId);
            return;
        }
        log.info("roomId: {}", roomId);
        try {
            String stringMessage = objectMapper.writeValueAsString(message);
            clientsMap.get(roomId)
                    .forEach(client ->
                            client.sendEvent(socketProperty.getGetMessageEvent(), stringMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMessage(SocketIOClient senderClient, Message message) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.CLIENT)
                .content(message.getContent())
                .room(message.getRoom())
                .username(message.getUsername())
                .build());
        sendSocketMessage(senderClient, storedMessage, message.getRoom());
    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.SERVER)
                .content(message)
                .room(room)
                .build());
        sendSocketMessage(senderClient, storedMessage, room);
    }

    public void addClients(SocketIOClient client) {
        Long roomId = getRoomId(client);
        if (!clientsMap.containsKey(roomId)) {
            clientsMap.put(roomId, new HashSet<>());
        }
        clientsMap.get(roomId).add(client);
    }

    public void removeClients(SocketIOClient client) {
        Long roomId = getRoomId(client);
        if (clientsMap.containsKey(roomId)) {
            clientsMap.get(roomId).remove(client);
        }
    }


    public Long getMemberId(SocketIOClient socketIOClient){
        var memberId = socketIOClient.<Long>get(socketProperty.getMemberKey());
        if (Objects.isNull(memberId)){
            throw new RuntimeException("MemberId is null");
        }
        return memberId;
    }

    public Long getRoomId(SocketIOClient socketIOClient){
        var roomId = socketIOClient.<Long>get(socketProperty.getRoomKey());
        if (Objects.isNull(roomId)){
            throw new RuntimeException("RoomId is null");
        }
        return roomId;
    }
}
