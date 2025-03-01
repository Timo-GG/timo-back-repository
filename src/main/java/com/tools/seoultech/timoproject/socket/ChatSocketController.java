package com.tools.seoultech.timoproject.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.tools.seoultech.timoproject.chat.dto.ChatMessageDTO;
import com.tools.seoultech.timoproject.chat.dto.ReadMessageRequest;
import com.tools.seoultech.timoproject.chat.service.ChatService;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SocketController
@Slf4j
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;

    @SocketMapping(endpoint = "send_message", requestCls = ChatMessageDTO.class)
    public void handleSendMessage(SocketIOClient senderClient, SocketIOServer server,
                                  ChatMessageDTO data) {
        Long senderId = senderClient .get("memberId"); // 서버 세션에서 memberId 조회
        String room = data.room();


        ChatMessageDTO chatMessage = ChatMessageDTO.builder()
                .room(room)
                .senderId(senderId)
                .content(data.content())
                .build();

        chatService.saveMessage(chatMessage);

        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("receive_message", chatMessage);
            }
        }    }

    @SocketMapping(endpoint = "read_message", requestCls = ReadMessageRequest.class)
    public void handleReadMessage(SocketIOClient client,
                                  SocketIOServer server,
                                  ReadMessageRequest request) {
        Long memberId = client.get("memberId");
        log.info("[read_message] memberId: {}, messageId: {}", memberId, request.messageId());
        // DB에서 읽음 처리 등
        // ...
    }

}
