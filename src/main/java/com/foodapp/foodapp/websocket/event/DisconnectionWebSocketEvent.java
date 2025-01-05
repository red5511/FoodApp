package com.foodapp.foodapp.websocket.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Schema(description = "Disconnection event via WebSocket")
@SuperBuilder
@ToString
public class DisconnectionWebSocketEvent extends WebSocketEvent {// User przesyla tego eventa kiedy sie rozlacza
    @Schema(required = true)
    public Long userId;
    @Schema(required = true)
    public Set<Long> companyIds;

    @JsonCreator
    public DisconnectionWebSocketEvent(@JsonProperty("eventType") final EventType eventType,
                                       @JsonProperty("userId") final Long userId,
                                       @JsonProperty("companyId") final Set<Long> companyIds) {
        super(eventType);
        this.userId = userId;
        this.companyIds = companyIds;
    }

}
