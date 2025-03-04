package com.foodapp.foodapp.websocket.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ApprovalDeadlinePassed extends WebSocketEvent {
    @Schema(required = true)
    public Long orderId;

    @JsonCreator
    public ApprovalDeadlinePassed(@JsonProperty("eventType") final EventType eventType) {
        super(eventType);
    }
}
