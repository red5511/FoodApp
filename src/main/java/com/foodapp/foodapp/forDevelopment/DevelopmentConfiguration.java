package com.foodapp.foodapp.forDevelopment;

import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.forDevelopment.scheduler.SchedulerForTestingService;
import com.foodapp.foodapp.order.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("TEST")
@EnableAspectJAutoProxy
public class DevelopmentConfiguration {
    @Value("${app.time-to-accept-order}")
    private Long timeToAcceptOrder;

    @Bean
    public DatabaseDataFaker databaseDataFaker(final CompanyRepository companyRepository,
                                               final UserRepository userRepository,
                                               final ProductRepository productRepository,
                                               final OrderRepository orderRepository,
                                               final OrderProductRepository orderProductRepository,
                                               final PasswordEncoder passwordEncoder) {
        return new DatabaseDataFaker(companyRepository,
                userRepository,
                productRepository,
                orderRepository,
                orderProductRepository,
                passwordEncoder
        );
    }

    @Bean
    //@Profile("ENABLE_SCHEDULER_WEBSOCKET_TEST")
    public SchedulerForTestingService schedulerForTestingService(@Lazy final WebSocketService webSocketService,
                                                                 final OrderRepository orderRepository,
                                                                 final CompanyRepository companyRepository,
                                                                 final ProductRepository productRepository) {
        return new SchedulerForTestingService(webSocketService,
                orderRepository,
                companyRepository,
                productRepository,
                timeToAcceptOrder
        );
    }

    @Bean
    public TechnicalContextProvider technicalContextProvider() {
        return new TechnicalContextProvider();
    }
}
