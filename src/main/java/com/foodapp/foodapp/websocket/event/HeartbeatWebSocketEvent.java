package com.foodapp.foodapp.websocket.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Schema(description = "Heartbeat event via WebSocket")
@SuperBuilder
@ToString
public class HeartbeatWebSocketEvent extends WebSocketEvent {
    @Schema(required = true)
    public Long userId;
    @Schema(required = true)
    public Long companyId;

    @JsonCreator
    public HeartbeatWebSocketEvent(@JsonProperty("eventType") final EventType eventType,
                                   @JsonProperty("userId") final Long userId,
                                   @JsonProperty("companyId") final Long companyId) {
        super(eventType);
        this.userId = userId;
        this.companyId = companyId;
    }
}
