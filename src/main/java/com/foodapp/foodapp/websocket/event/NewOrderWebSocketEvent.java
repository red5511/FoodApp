package com.foodapp.foodapp.websocket.event;

import com.foodapp.foodapp.order.dto.OrderDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Event for a new order created via WebSocket")
public class NewOrderWebSocketEvent extends WebSocketEvent {
    @Schema(description = "order", required = true)
    private final OrderDto order;

    public NewOrderWebSocketEvent(final EventType eventType, final OrderDto order) {
        super(eventType);
        this.order = order;
    }
}
