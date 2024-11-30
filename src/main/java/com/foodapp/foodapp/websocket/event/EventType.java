package com.foodapp.foodapp.websocket.event;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Event types for WebSocket events")
public enum EventType {
    NEW_ORDER, HEARTBEAT
}
