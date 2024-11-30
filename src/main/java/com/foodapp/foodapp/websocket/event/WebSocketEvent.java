package com.foodapp.foodapp.websocket.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "Base class for WebSocket events")
public class WebSocketEvent {
    @Schema(description = "event type", required = true)
    private EventType eventType;
}
