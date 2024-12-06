package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.websocket.event.DisconnectionWebSocketEvent;
import com.foodapp.foodapp.websocket.event.HeartbeatWebSocketEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebSocketEventHandler {
    private final CacheService cacheService;
    private final WebSocketEventSender webSocketEventSender;

    //todo zastanowic sie czy mi to az tak potrzebne jest bo chyba bedzie to ruchalo mi performace
    public void handleHeartbeatWebSocketEvent(final HeartbeatWebSocketEvent event) {
        cacheService.prepareCacheForReceivingOrders(event.getUserId(), event.getCompanyId());
    }

    public void handleDisconnectionWebSocketEvent(final DisconnectionWebSocketEvent event) {
        cacheService.removeCacheFroReceivingOrders(event.getUserId(), event.getCompanyId());
        webSocketEventSender.sendServerSideDisconnectionWebSocketEvent(event.getUserId(), event.getOrderReceivingTopicName());
    }
}
