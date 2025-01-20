package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.websocket.event.DisconnectionWebSocketEvent;
import com.foodapp.foodapp.websocket.event.HeartbeatWebSocketEvent;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class WebSocketEventHandler {
    private final CacheService cacheService;
    private final WebSocketEventSender webSocketEventSender;
    private final CompanyRepository companyRepository;

    //todo zastanowic sie czy mi to az tak potrzebne jest bo chyba bedzie to ruchalo mi performace
    public void handleHeartbeatWebSocketEvent(final HeartbeatWebSocketEvent event) {
        cacheService.prepareCacheForReceivingOrders(event.getUserId(), event.getCompanyIds(), Set.of());
    }

    public void handleDisconnectionWebSocketEvent(final DisconnectionWebSocketEvent event) {
        cacheService.removeCacheFroReceivingOrders(event.getUserId(), event.getCompanyIds());
        var eventToSend = EventMapper.createServerSideDisconnectionWebSocketEvent(event.getUserId());
        var topicNames = companyRepository.findAllById(event.getCompanyIds()).stream()
                .map(Company::getWebSocketTopicName)
                .collect(Collectors.toSet());
        webSocketEventSender.fireAll(eventToSend, topicNames);
    }
}
