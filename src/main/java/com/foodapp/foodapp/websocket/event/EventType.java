package com.foodapp.foodapp.websocket.event;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Event types for WebSocket events")
public enum EventType {
    NEW_ORDER, HEARTBEAT, DISCONNECTION, SERVER_SIDE_DISCONNECTION, APPROVED_ORDER, REJECTED_ORDER, APPROVAL_DEADLINE_PASSED
}
