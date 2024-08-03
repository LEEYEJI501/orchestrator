package com.example.orchestrator;

import com.example.orchestrator.constants.TopicConstants;
import com.example.orchestrator.dto.InitialStockDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"jwt.secret=mysecurekeywhichis32characterslong"})
public class WebSocketClientAndInitStockDataResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testReceiveInitialData() throws ExecutionException, InterruptedException, JsonProcessingException {
        WebSocketStompClient stompClient = new WebSocketStompClient(
                new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));

        String url = "ws://localhost:" + "3000" + "/v1/ws";
        StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Connected to WebSocket server");
            }
        }).get();

        Map<String, String> message = Map.of("marketName", "KOSPI", "code", "005930", "timeframe", "5years");
        byte[] payload = objectMapper.writeValueAsBytes(message);

        session.send("/app/initialData", payload);

        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        session.subscribe(TopicConstants.TOPIC_INITIAL_DATA_PREFIX, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received message: " + payload);
                blockingQueue.offer((String) payload);
            }
        });

        // 타임아웃을 20초로 설정
        String response = blockingQueue.poll(120, TimeUnit.SECONDS);
        assertThat(response).isNotNull();

        InitialStockDto initialStockData = objectMapper.readValue(response, InitialStockDto.class);

        System.out.println("Received Initial Stock Data: " + initialStockData);

        assertThat(initialStockData).isNotNull();
        assertThat(initialStockData.getStockData()).isNotEmpty();
        assertThat(initialStockData.getMovingAverages()).isNotNull();
        assertThat(initialStockData.getBollingerBands()).isNotNull();
        assertThat(initialStockData.getMacd()).isNotNull();
        assertThat(initialStockData.getRsi()).isNotNull();

        // WebSocket 연결을 종료하지 않도록 잠시 대기
        TimeUnit.SECONDS.sleep(10);
    }
}
