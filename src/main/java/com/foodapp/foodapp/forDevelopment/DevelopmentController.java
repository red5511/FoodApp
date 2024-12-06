package com.foodapp.foodapp.forDevelopment;

import com.foodapp.foodapp.websocket.event.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/development")
@AllArgsConstructor
@Tag(name = "Development") //todo pamietac zeby to wywalic przy wgraniu na proda
public class DevelopmentController {
    @PostMapping("/forGeneratingApi")
    public ResponseEntity<NewOrderWebSocketEvent> initOrderWebSocketTopic(final @RequestBody WebSocketEvent request) {
        return ResponseEntity.ok(new NewOrderWebSocketEvent(null, null));
    }

    @PostMapping("/forGeneratingApi2")
    public ResponseEntity<HeartbeatWebSocketEvent> heartbeatWebSocketEvent(final @RequestBody WebSocketEvent request) {
        return ResponseEntity.ok(new HeartbeatWebSocketEvent(null, null, null));
    }

    @PostMapping("/forGeneratingApi3")
    public ResponseEntity<DisconnectionWebSocketEvent> disconnectionWebSocketEvent(final @RequestBody WebSocketEvent request) {
        return ResponseEntity.ok(new DisconnectionWebSocketEvent(null, null, null));
    }

    @PostMapping("/forGeneratingApi4")
    public ResponseEntity<ServerSideDisconnectionWebSocketEvent> ehh(final @RequestBody WebSocketEvent request) {
        return ResponseEntity.ok(new ServerSideDisconnectionWebSocketEvent(null));
    }

    @PostMapping("/forGeneratingApi5")
    public ResponseEntity<Void> ehh2(final @RequestBody RejectedOrderWebSocketEvent request) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forGeneratingApi6")
    public ResponseEntity<Void> ehh3(final @RequestBody ApprovedOrderWebSocketEvent request) {
        return ResponseEntity.ok().build();
    }
}
