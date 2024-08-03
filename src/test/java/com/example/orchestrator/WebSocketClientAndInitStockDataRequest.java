package com.example.orchestrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"jwt.secret=mysecurekeywhichis32characterslong"})
public class WebSocketClientAndInitStockDataRequest {
    @Test
    public void testInitialDataRequest() throws ExecutionException, InterruptedException, JsonProcessingException {
        WebSocketStompClient stompClient = new WebSocketStompClient(
                new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));

        String url = "ws://localhost:" + "3000" + "/v1/ws";
        StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                // handle frame
            }
        }).get();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> message = Map.of("marketName", "KOSPI", "code", "005930", "timeframe", "5years");
        byte[] payload = objectMapper.writeValueAsBytes(message);

        session.send("/app/initialData", payload);
    }
}
