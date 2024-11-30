package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.websocket.request.InitOrderWebSocketTopicRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/websocket")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "WebSocket")
public class WebSocketController {
    private final WebSocketService webSocketService;

    @PreAuthorize("hasAuthority('VIEW_LIVE_PANEL')")
    @PostMapping("/init-main-topic")
    public ResponseEntity<Void> initOrderWebSocketTopic(final @RequestBody InitOrderWebSocketTopicRequest request) {
        webSocketService.initMainTopic(request);
        return ResponseEntity.ok().build();
    }
}
