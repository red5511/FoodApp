package com.foodapp.foodapp.websocket.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InitOrderWebSocketTopicRequest {
    @Schema(description = "company id", required = true)
    private Long companyId;
    @Schema(description = "websocket topic name", required = true)
    private String webSocketTopicName;
}
