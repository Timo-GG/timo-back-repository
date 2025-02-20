package com.tools.seoultech.timoproject.chat.socket;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.tools.seoultech.timoproject.auth.jwt.JwtResolver;
import com.tools.seoultech.timoproject.chat.model.ChatRoom;
import com.tools.seoultech.timoproject.chat.service.ChatRoomService;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIOConnectListener implements ConnectListener {

    private final JwtResolver jwtResolver;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final SocketService socketService;
    private final SocketProperty socketProperty;

    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        Member member = getMember(socketIOClient);
        ChatRoom room = getRoom(socketIOClient);
        allowOrigin(socketIOClient);
        saveMemberId(socketIOClient, member.getId());
        saveRoomId(socketIOClient, room.getId());
        joinRoom(socketIOClient, room);
        socketService.addClients(socketIOClient);

        log.info("Socket ID[{}]  Member Id {} Room Id {} Connected to socket",
                socketIOClient.getSessionId().toString(), member.getId(), room.getId());
    }

    private Member getMember(SocketIOClient socketIOClient) {
        String accessToken = socketIOClient
                .getHandshakeData()
                .getSingleUrlParam("auth");
        Long memberId = jwtResolver.getMemberIdFromAccessToken(accessToken);
        return memberService.getById(memberId);
    }

    private ChatRoom getRoom(SocketIOClient socketIOClient) {
        Long roomId = Long.valueOf(
                socketIOClient
                        .getHandshakeData()
                        .getSingleUrlParam("roomId")
        );
        validRoomId(roomId);
        return chatRoomService.getById(roomId);
    }

    private void saveMemberId(SocketIOClient socketIOClient, Long memberId) {
        socketIOClient.set(socketProperty.getMemberKey(), memberId);
    }

    private void saveRoomId(SocketIOClient socketIOClient, Long roomId) {
        socketIOClient.set(socketProperty.getRoomKey(), roomId);
    }

    private void joinRoom(SocketIOClient socketIOClient, ChatRoom room) {
        String stringRoomId = String.valueOf(room.getId());
        socketIOClient.joinRoom(stringRoomId);
    }

    private void validRoomId(Long roomId) {
        if(roomId == null){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void allowOrigin(SocketIOClient socketIOClient) {
        socketIOClient
                .getHandshakeData()
                .getHttpHeaders()
                .add("Access-Control-Allow-Origin", "*");
    }
}
