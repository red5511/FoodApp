package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.websocket.event.*;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@AllArgsConstructor
public class WebSocketEventSender {
    public final static String MAIN_TOPIC = "/main";
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNewOrderWebSocketEvent(final OrderDto orderDto, final String orderReceivingTopicName) {
        var event = NewOrderWebSocketEvent.builder()
                .order(orderDto)
                .eventType(EventType.NEW_ORDER)
                .build();
        messagingTemplate.convertAndSendToUser(orderReceivingTopicName, MAIN_TOPIC, event);
    }

    public void sendServerSideDisconnectionWebSocketEvent(final Long userId, final String orderReceivingTopicName) {
        var event = ServerSideDisconnectionWebSocketEvent.builder()
                .userId(userId)
                .eventType(EventType.SERVER_SIDE_DISCONNECTION)
                .build();
        messagingTemplate.convertAndSendToUser(orderReceivingTopicName, MAIN_TOPIC, event);
    }

    public void sendRejectedOrderWebSocketEvent(final Long orderId, final String orderReceivingTopicName) {
        var event = RejectedOrderWebSocketEvent.builder()
                .orderId(orderId)
                .eventType(EventType.REJECTED_ORDER)
                .build();
        messagingTemplate.convertAndSendToUser(orderReceivingTopicName, MAIN_TOPIC, event);
    }

    public void sendApprovedOrderWebSocketEvent(final Long orderId, final String orderReceivingTopicName) {
        var event = ApprovedOrderWebSocketEvent.builder()
                .orderId(orderId)
                .eventType(EventType.APPROVED_ORDER)
                .build();
        messagingTemplate.convertAndSendToUser(orderReceivingTopicName, MAIN_TOPIC, event);
    }
}
