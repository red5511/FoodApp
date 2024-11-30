package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.administration.cache.UsersConnectedToWebCacheWrapper;
import com.foodapp.foodapp.administration.cache.UsersConnectedToWebSocketEntry;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.user.UserMapper;
import com.foodapp.foodapp.websocket.request.InitOrderWebSocketTopicRequest;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@AllArgsConstructor
public class WebSocketService {
    private final static String MAIN_TOPIC = "/main";
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersConnectedToWebCacheWrapper cacheWrapper;
    private final ContextProvider contextProvider;
    private final SchedulerForTestingService schedulerForTestingService;


    public void sendNewOrderToTopic(final String topicName, final OrderDto orderDto) {
        messagingTemplate.convertAndSendToUser(topicName, MAIN_TOPIC, orderDto);
    }

    public void initMainTopic(final InitOrderWebSocketTopicRequest request) {
        var user = contextProvider.getUser().orElseThrow(() -> new SecurityException("No user"));
        cacheWrapper.put(user.getId(), UsersConnectedToWebSocketEntry.builder()
                .companyId(request.getCompanyId())
                .user(UserMapper.toUserDto(user))
                .build()
        );
        schedulerForTestingService.addNewTopicToSend(request.getWebSocketTopicName());
    }
}
