package com.foodapp.foodapp.scheduler;


import com.foodapp.foodapp.company.Company;
import com.foodapp.foodapp.order.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Slf4j
public class SchedulerForTestingService {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 30000)
    public void sendTestOrderToWebsocket() {
        var order = getOrderForTest();
        log.info("Sending order to ts");
        orderService.sendNewOrdersNotification("maciekfranczak@onet.eu", order);
    }

    public OrderDto getOrderForTest() {
        var order = orderRepository.findById(1L)
                .orElse(Order.builder()
                        .deliveryTime(LocalDateTime.now())
                        .deliveryAddress("Sikorskiego 43")
                        .orderType(OrderType.GLOVO)
                        .customerName("Maciek Franczak")
                        .description("Zautomatyzowane zamowienie")
                        .status(OrderStatus.WAITING_FOR_ACCEPTANCE)
                        .price(BigDecimal.TEN)
                        .company(Company.builder()
                                .build())
                        .build());
        return orderService.mapToOrderDto(order);
    }

}
