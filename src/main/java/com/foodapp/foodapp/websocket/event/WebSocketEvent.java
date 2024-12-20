package com.foodapp.foodapp.websocket.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Schema(description = "Base class for WebSocket events")
@SuperBuilder
public class WebSocketEvent {
    @Schema(description = "event type", required = true)
    private EventType eventType;

    @JsonCreator
    public WebSocketEvent(@JsonProperty("eventType") final EventType eventType) {
        this.eventType = eventType;
    }
}
