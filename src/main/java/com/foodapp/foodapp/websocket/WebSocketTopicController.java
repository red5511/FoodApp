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
        System.out.println("HEARTBEAT");
        eventHandler.handleHeartbeatWebSocketEvent(event);
    }

    @MessageMapping("/disconnect") // Messages sent to `/app/disconnect` arrive here
    public void handleDisconnectionEvent(@Payload DisconnectionWebSocketEvent event) {
        System.out.println("DC from socket");
        eventHandler.handleDisconnectionWebSocketEvent(event);
    }
}
