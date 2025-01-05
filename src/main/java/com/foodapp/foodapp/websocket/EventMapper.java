package com.foodapp.foodapp.websocket;


import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.websocket.event.*;

public class EventMapper {
    public static ApprovedOrderWebSocketEvent createApprovedOrderWebSocketEvent(final Long orderId) {
        return ApprovedOrderWebSocketEvent.builder()
                .orderId(orderId)
                .eventType(EventType.APPROVED_ORDER)
                .build();
    }

    public static RejectedOrderWebSocketEvent createRejectedOrderWebSocketEvent(final Long orderId) {
        return RejectedOrderWebSocketEvent.builder()
                .orderId(orderId)
                .eventType(EventType.REJECTED_ORDER)
                .build();
    }

    public static ServerSideDisconnectionWebSocketEvent createServerSideDisconnectionWebSocketEvent(final Long userId) {
        return ServerSideDisconnectionWebSocketEvent.builder()
                .userId(userId)
                .eventType(EventType.SERVER_SIDE_DISCONNECTION)
                .build();
    }

    public static NewOrderWebSocketEvent createNewOrderWebSocketEvent(final OrderDto orderDto) {
        return NewOrderWebSocketEvent.builder()
                .order(orderDto)
                .eventType(EventType.NEW_ORDER)
                .build();
    }

    public static ApprovalDeadlinePassed createApprovalDeadlinePassedWebSocketEvent(final Long orderId) {
        return ApprovalDeadlinePassed.builder()
                .orderId(orderId)
                .eventType(EventType.APPROVAL_DEADLINE_PASSED)
                .build();
    }
}
