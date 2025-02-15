package com.tools.seoultech.timoproject.chat;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("[Socket] 소켓 통신 통합 테스트")
public class SocketCommunicationIntegrationTest {

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private Integer port;

    private Socket client1;
    private Socket client2;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        // 기본 옵션 객체 생성 후, 각 클라이언트에 대해 query 파라미터를 개별 설정합니다.
        IO.Options options1 = new IO.Options();
        options1.query = "room=test&username=client1";
        IO.Options options2 = new IO.Options();
        options2.query = "room=test&username=client2";

        // 서버 URL 생성 (예: http://localhost:8085)
        String baseUrl = "http://" + host + ":" + port;
        client1 = IO.socket(baseUrl, options1);
        client2 = IO.socket(baseUrl, options2);
    }

    @AfterEach
    public void tearDown() {
        if (client1 != null) {
            client1.disconnect();
            client1.close();
        }
        if (client2 != null) {
            client2.disconnect();
            client2.close();
        }
    }

    @Test
    public void testMessageBroadcastBetweenClients() throws InterruptedException {
        List<String> receivedMessages = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        // client2에서 "read_message" 이벤트를 리스닝
        client2.on("read_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args.length > 0 && args[0] instanceof JSONObject) {
                    JSONObject jsonMessage = (JSONObject) args[0];
                    String content = jsonMessage.optString("content");
                    System.out.println("client2 received message: " + content);
                    receivedMessages.add(content);
                    if ("Hello, World!".equals(content)) {
                        latch.countDown();
                    }
                }
            }
        });

        // 두 클라이언트 연결
        client1.connect();
        client2.connect();

        // 연결 및 초기 환영 메시지 처리 시간을 위해 대기 (필요 시 조정)
        Thread.sleep(2000);

        // client1이 "send_message" 이벤트로 메시지 전송
        JSONObject message = new JSONObject();
        message.put("content", "Hello, World!");
        message.put("room", "test");
        message.put("username", "client1");

        System.out.println("client1 emits test message: " + message);
        client1.emit("send_message", message);

        // 최대 5초간 "Hello, World!" 메시지 수신을 대기
        boolean messageReceived = latch.await(5, TimeUnit.SECONDS);
        System.out.println("All received messages: " + receivedMessages);

        assertTrue(messageReceived, "client2는 client1이 보낸 'Hello, World!' 메시지를 수신해야 합니다.");
        assertTrue(receivedMessages.contains("Hello, World!"),
                "수신된 메시지 목록에 'Hello, World!' 메시지가 포함되어야 합니다.");
    }
}
