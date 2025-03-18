package com.foodapp.foodapp.forDevelopment.websocket;

import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.rabbitMQ.RabbitMQSender;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.websocket.WebSocketEventSender;
import com.foodapp.foodapp.websocket.WebSocketServiceForTesting;
import com.foodapp.foodapp.websocket.WebSocketServiceInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;

@Configuration
@ConditionalOnProperty(name = "app.feature-toggle.enable-sending-orders", havingValue = "true", matchIfMissing = false)
@EnableAspectJAutoProxy
public class WebSocketDevelopmentConfiguration {
    @Value("${app.time-to-accept-order}")
    private Long timeToAcceptOrder;

    @Bean
    public SchedulerForTestingService schedulerForTestingService(@Lazy final WebSocketServiceForTesting webSocketService,
                                                                 final OrderRepository orderRepository,
                                                                 final CompanyRepository companyRepository,
                                                                 final ProductRepository productRepository,
                                                                 final CacheService cacheService,
                                                                 final RabbitMQSender rabbitMQSender,
                                                                 final CustomOrderIdGenerator customOrderIdGenerator) {
        return new SchedulerForTestingService(webSocketService,
                orderRepository,
                companyRepository,
                productRepository,
                timeToAcceptOrder,
                cacheService,
                rabbitMQSender,
                customOrderIdGenerator
        );
    }

    @Bean
    public WebSocketServiceInterface webSocketServiceForTesting(final ContextProvider contextProvider,
                                                                final SchedulerForTestingService schedulerForTestingService,
                                                                final CacheService cacheService,
                                                                final WebSocketEventSender webSocketEventSender,
                                                                final RabbitMQSender rabbitMQSender) {
        return new WebSocketServiceForTesting(
                contextProvider,
                schedulerForTestingService,
                cacheService,
                webSocketEventSender,
                rabbitMQSender
        );
    }
}
