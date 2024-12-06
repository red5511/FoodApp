package com.foodapp.foodapp.websocket.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class ServerSideDisconnectionWebSocketEvent extends WebSocketEvent {
    // Serwer przesyla tego eventa kiedy ma nastapic rozlaczenia usera

    @Schema(required = true)
    public Long userId;

    @JsonCreator
    public ServerSideDisconnectionWebSocketEvent(@JsonProperty("userId") final Long userId) {
        super(EventType.SERVER_SIDE_DISCONNECTION);
        this.userId = userId;
    }

}
