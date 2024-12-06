package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.websocket.event.DisconnectionWebSocketEvent;
import com.foodapp.foodapp.websocket.event.HeartbeatWebSocketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketTopicController {
    private final WebSocketEventHandler eventHandler;

    @MessageMapping("/heartbeat") // Messages sent to `/app/heartbeat` arrive here
    public void handleHeartbeatEvent(@Payload HeartbeatWebSocketEvent event) {
        eventHandler.handleHeartbeatWebSocketEvent(event);
    }

    @MessageMapping("/disconnect") // Messages sent to `/app/heartbeat` arrive here
    public void handleDisconnectionEvent(@Payload DisconnectionWebSocketEvent event) {
        eventHandler.handleDisconnectionWebSocketEvent(event);
    }
}
