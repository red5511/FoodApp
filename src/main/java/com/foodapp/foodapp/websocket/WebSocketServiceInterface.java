package com.foodapp.foodapp.websocket;

import com.foodapp.foodapp.websocket.request.InitOrderWebSocketTopicRequest;

public interface WebSocketServiceInterface {
    void initMainTopic(final InitOrderWebSocketTopicRequest request);
}
