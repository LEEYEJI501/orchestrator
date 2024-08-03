package com.example.orchestrator.controller;

import com.example.orchestrator.constants.TopicConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.grpc.*;
import com.example.orchestrator.util.JsonUtil;
import java.util.Map;
import java.util.List;
import java.util.function.Function;
import org.springframework.messaging.handler.annotation.MessageMapping;
import com.example.orchestrator.service.KafkaProducerService;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final JsonUtil jsonUtil;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, JsonUtil jsonUtil, KafkaProducerService kafkaProducerService) {
        this.messagingTemplate = messagingTemplate;
        this.jsonUtil = jsonUtil;
        this.kafkaProducerService = kafkaProducerService;
    }

    @MessageMapping("/initialData")
    public void handleInitialDataRequest(Map<String, String> request) {
        String marketName = request.get("marketName");
        String code = request.get("code");
        String timeframe = request.get("timeframe");

        kafkaProducerService.sendInitialDataRequest(marketName, code, timeframe);
    }

    public void sendActivityUpdate(List<Long> followerIds, Function<Long, Response> getLatestActivityForFollowees) {
        for (Long followerId : followerIds) {
            Response response = getLatestActivityForFollowees.apply(followerId);
            String result = response.getResult();

            if (!result.isEmpty()) {
                Map<String, Object> latestActivityMap = jsonUtil.getMapByKey(result, "latest_activity");
                if (latestActivityMap != null && !latestActivityMap.isEmpty()) {
                    String message = latestActivityMap.get("message").toString();
                    if (!message.isEmpty()) {
                        this.messagingTemplate.convertAndSend(TopicConstants.TOPIC_ACTIVITIES_PREFIX + followerId, message);
                    }
                }
            }
        }
    }

    public void sendLogoutMessage(Long userId) {
        this.messagingTemplate.convertAndSend(TopicConstants.TOPIC_LOGOUT_PREFIX + userId, "logout");
    }
}