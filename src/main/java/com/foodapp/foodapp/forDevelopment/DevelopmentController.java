package com.foodapp.foodapp.forDevelopment;

import com.foodapp.foodapp.websocket.event.NewOrderWebSocketEvent;
import com.foodapp.foodapp.websocket.event.WebSocketEvent;
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
}
