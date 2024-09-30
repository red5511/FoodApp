package com.foodapp.foodapp.forDevelopment.scheduler;


import com.foodapp.foodapp.company.Company;
import com.foodapp.foodapp.company.CompanyRepository;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
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
    private final CompanyRepository companyRepository;

    @Scheduled(fixedRate = 15000)
    @TechnicalContextDev
    public void sendTestOrderToWebsocket() {
        var order = createOrderForTest();
        order = orderRepository.save(order);
        log.info("Sending order to ts");
        orderService.sendNewOrdersNotification("maciekfranczak@onet.eu", OrderService.mapToOrderDto(order));
    }

    public Order createOrderForTest() {
        Company company =
                companyRepository.findById(1L).orElseThrow(() -> new IllegalStateException("Send new order - wrong company id"));
        return Order.builder()
                .deliveryTime(LocalDateTime.now())
                .deliveryAddress("Sikorskiego 43")
                .orderType(OrderType.GLOVO)
                .customerName("Maciek Franczak")
                .description(
                        "Poprosze osobno frytki i cole bez lodu. W razie problemow ze znalezeniem numry zostawic na portierni"
                )
                .status(OrderStatus.WAITING_FOR_ACCEPTANCE)
                .price(BigDecimal.TEN)
                .company(Company.builder()
                        .build())
                .company(company)
                .build();
    }

}
