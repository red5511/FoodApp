package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.websocket.event.WebSocketEvent;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Set;

@AllArgsConstructor
public class WebSocketEventSender {
    public final static String MAIN_TOPIC = "/main";
    private final SimpMessagingTemplate messagingTemplate;

    public void fire(final WebSocketEvent event, final String orderReceivingTopicName) {
        messagingTemplate.convertAndSendToUser(orderReceivingTopicName, MAIN_TOPIC, event);
    }

    public void fireAll(final WebSocketEvent event, final Set<String> orderReceivingTopicNames) {
        orderReceivingTopicNames.forEach(topic -> fire(event, topic));
    }
}
