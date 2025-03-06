package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.rabbitMQ.RabbitMQSender;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.websocket.request.InitOrderWebSocketTopicRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@AllArgsConstructor
@Slf4j
public class WebSocketServiceForTesting implements WebSocketServiceInterface{
    private final ContextProvider contextProvider;
    private final SchedulerForTestingService schedulerForTestingService;
    private final CacheService cacheService;
    private final WebSocketEventSender webSocketEventSender;
    private final RabbitMQSender rabbitMQSender;


    public void sendNewOrderToTopic(final String topicName,
                                    final OrderDto orderDto,
                                    final Long companyId) {
        if (cacheService.validateIfCompanyIsReceivingOrders(companyId)) {
            System.out.println("jestem w ifie");
            var event = EventMapper.createNewOrderWebSocketEvent(orderDto);
            webSocketEventSender.fire(event, topicName);
        } else {
            System.out.println("NIE MA AKTYWNEGO USERA");
            //todo tutaj bym odrazu odpowiedzial glovo ze mnie moga w dupe pocalowac a nie prfzyjem
            schedulerForTestingService.removeTopicFromSending(topicName);
        }
    }

    public void initMainTopic(final InitOrderWebSocketTopicRequest request) {
        var allCompanyIds = new HashSet<>(request.getCompanyIdsToAdd());
        allCompanyIds.addAll(request.getCompanyIdsToRemove());

        contextProvider.validateCompanyAccess(allCompanyIds);
        var user = contextProvider.getUser().orElseThrow(() -> new SecurityException("No user"));

        cacheService.prepareCacheForReceivingOrders(user.getId(), request.getCompanyIdsToAdd(), request.getCompanyIdsToRemove());
        schedulerForTestingService.updateTopicToSend(request.getCompanyIdsToAdd(), request.getCompanyIdsToRemove());
    }
}
