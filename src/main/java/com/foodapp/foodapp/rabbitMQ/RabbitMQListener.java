package com.foodapp.foodapp.rabbitMQ;

import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.order.sql.Order;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.websocket.EventMapper;
import com.foodapp.foodapp.websocket.WebSocketEventSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.foodapp.foodapp.config.RabbitMQConfig.ORDER_DELAY_PROCESS_QUEUE;

@AllArgsConstructor
@Slf4j
public class RabbitMQListener {
    private final OrderRepository orderRepository;
    private final WebSocketEventSender webSocketEventSender;

    @RabbitListener(queues = ORDER_DELAY_PROCESS_QUEUE)
    @TechnicalContextDev
    @Transactional
    public void processOrder(final RabbitMQEvent rabbitMQEvent) {
        var orderId = rabbitMQEvent.getOrderId();
        System.out.println("Handluje Rabita");
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE) {
                order.setStatus(OrderStatus.NOT_ACCEPTED);
                orderRepository.save(order);
                var event = EventMapper.createApprovalDeadlinePassedWebSocketEvent(orderId);
                var topicName = rabbitMQEvent.getTopicToSend();
                webSocketEventSender.fire(event, topicName);
                log.info("RabbitMq: Order " + orderId + " rejected due to timeout to " + topicName);
            } else {
                log.info("RabbitMq: Order " + orderId + " was already processed probably by user");
            }
        }
    }
}
