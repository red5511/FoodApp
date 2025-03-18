package com.foodapp.foodapp.config;

import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.rabbitMQ.RabbitMQListener;
import com.foodapp.foodapp.rabbitMQ.RabbitMQSender;
import com.foodapp.foodapp.websocket.WebSocketEventSender;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "app.feature-toggle.enable-sending-orders", havingValue = "true", matchIfMissing = false)
public class RabbitMQConfig {

    public static final String ORDER_DELAY_EXCHANGE = "order.delay.exchange";
    public static final String ORDER_DELAY_QUEUE = "order.delay.queue";
    public static final String ORDER_DELAY_ROUTING_KEY = "order.delay";
    public static final String ORDER_DELAY_PROCESS_QUEUE = "order.process";

    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange(ORDER_DELAY_EXCHANGE);
    }

    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder.durable(ORDER_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", "") // Direct it to the default exchange
                .withArgument("x-dead-letter-routing-key", "order.process")
                .withArgument("x-message-ttl", 90000) // delay
                .build();
    }

    @Bean
    public Binding orderDelayBinding(Queue orderDelayQueue, DirectExchange orderDelayExchange) {
        return BindingBuilder.bind(orderDelayQueue).to(orderDelayExchange).with("order.delay");
    }

    @Bean
    public Queue orderProcessingQueue() {
        return new Queue("order.process");
    }

    @Bean
    public RabbitMQSender rabbitMQSender(final RabbitTemplate rabbitTemplate) {
        return new RabbitMQSender(rabbitTemplate);
    }

    @Bean
    public RabbitMQListener rabbitMQListener(final OrderRepository orderRepository,
                                             final WebSocketEventSender webSocketEventSender) {
        return new RabbitMQListener(orderRepository, webSocketEventSender);
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        //todo moze pomyslec o wyniesieniu tego do applications.yaml
        converter.setAllowedListPatterns(List.of("com.foodapp.foodapp.rabbitMQ.*"));
        return converter;
    }
}