package com.foodapp.foodapp.forDevelopment.websocket;

import com.foodapp.foodapp.websocket.WebSocketServiceInterface;
import com.foodapp.foodapp.websocket.request.InitOrderWebSocketTopicRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketServiceMock implements WebSocketServiceInterface {
    @Override
    public void initMainTopic(InitOrderWebSocketTopicRequest request) {
        log.info("Mocking initMainTopic");
    }
}
