package com.foodapp.foodapp.rabbitMQ;

import com.foodapp.foodapp.config.RabbitMQConfig;
import com.foodapp.foodapp.order.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@AllArgsConstructor
@Slf4j
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Order order) {
        log.info("Send RabbitMQ Order ID: " + order.getId());
        var event = RabbitMQEvent.builder()
                .orderId(order.getId())
                .topicToSend(order.getCompany().getWebSocketTopicName())
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_DELAY_EXCHANGE, RabbitMQConfig.ORDER_DELAY_ROUTING_KEY, event);
    }
}
