package com.tools.seoultech.timoproject.chat.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.tools.seoultech.timoproject.chat.dto.MessageResponse;
import com.tools.seoultech.timoproject.chat.model.Message;

public interface SocketService {

        void sendSocketMessage(SocketIOClient senderClient, Message message, String room);

        void saveMessage(SocketIOClient senderClient, Message message);

        void saveInfoMessage(SocketIOClient senderClient, String message, String room);

        Long getMemberId(SocketIOClient socketIOClient);

        Long getRoomId(SocketIOClient socketIOClient);

        void sendMessage(SocketIOClient senderClient, MessageResponse messageResponse);

        void addClients(SocketIOClient client);

        void removeClients(SocketIOClient client);
}
