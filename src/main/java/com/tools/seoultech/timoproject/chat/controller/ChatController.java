package com.tools.seoultech.timoproject.chat.controller;


import com.corundumstudio.socketio.SocketIOClient;
import com.tools.seoultech.timoproject.chat.dto.*;
import com.tools.seoultech.timoproject.chat.facade.ChatRoomFacade;
import com.tools.seoultech.timoproject.chat.socket.SocketService;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.global.annotation.SocketController;
import com.tools.seoultech.timoproject.global.annotation.SocketMapping;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@SocketController("/event/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomFacade chatRoomFacade;
    private final SocketService socketService;

    @SocketMapping(endpoint = "/text", requestCls = SendMessageRequest.class)
    public MessageResponse sendTextMessage(
            SocketIOClient client,
            @Valid SendMessageRequest request) {
        Long memberId = socketService.getMemberId(client);
        Long roomId = socketService.getRoomId(client);

        return chatRoomFacade.sendTextMessage(
                client,
                memberId,
                roomId,
                request.content());
    }

    @SocketMapping(endpoint = "/receive", requestCls = ReceiveMessageRequest.class)
    public void receiveMessage(
        SocketIOClient client,
        ReceiveMessageRequest request){
        Long memberId = socketService.getMemberId(client);
        Long roomId = socketService.getRoomId(client);

        chatRoomFacade.receiveMessage(
            client,
            memberId,
            roomId,
            request.messageId());
    }

    @GetMapping("/room/{roomId}/message")
    public ResponseEntity<APIDataResponse<GetMessageListResponse>> getMessages(
        @CurrentMemberId long memberId,
        @PathVariable long roomId,
        @RequestParam(required = false) Long lastMessageId
    ){
        var response = GetMessageListResponse.from(
            chatRoomFacade.getMessages(memberId, roomId, lastMessageId)
        );

        return ResponseEntity.ok(APIDataResponse.of(response));
    }

    @GetMapping("/room/{roomId}/message/recent")
    public ResponseEntity<APIDataResponse<GetLastSeenMessageResponse>> getRecentMessages(
        @CurrentMemberId long memberId,
        @PathVariable long roomId
    ){
        var response = GetLastSeenMessageResponse.from(
            chatRoomFacade.getRecentMessageId(memberId, roomId)
        );

        return ResponseEntity.ok(APIDataResponse.of(response));
    }

}
