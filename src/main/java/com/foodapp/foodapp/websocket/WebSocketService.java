package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.websocket.request.InitOrderWebSocketTopicRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebSocketService {
    private final ContextProvider contextProvider;
    private final SchedulerForTestingService schedulerForTestingService;
    private final CacheService cacheService;
    private final WebSocketEventSender webSocketEventSender;


    public void sendNewOrderToTopic(final String topicName,
                                    final OrderDto orderDto,
                                    final Long companyId) {
        if (!cacheService.validateIfCompanyIsReceivingOrders(companyId)) {
            System.out.println("NIE MA AKTYWNEGO USERA");
            //todo tutaj bym odrazu odpowiedzial glovo ze mnie moga w dupe pocalowac a nie prfzyjem
            schedulerForTestingService.removeTopicFromSending(topicName);
        }
        webSocketEventSender.sendNewOrderWebSocketEvent(orderDto, topicName);
    }

    public void initMainTopic(final InitOrderWebSocketTopicRequest request) {
        var user = contextProvider.getUser().orElseThrow(() -> new SecurityException("No user"));
        cacheService.prepareCacheForReceivingOrders(user.getId(), request.getCompanyId());
        schedulerForTestingService.addNewTopicToSend(request.getWebSocketTopicName());
    }
}
