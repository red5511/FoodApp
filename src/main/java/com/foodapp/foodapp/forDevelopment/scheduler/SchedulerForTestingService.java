package com.foodapp.foodapp.forDevelopment.scheduler;


import com.foodapp.foodapp.administration.company.Company;
import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.order.*;
import com.foodapp.foodapp.product.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

@AllArgsConstructor
@Slf4j
public class SchedulerForTestingService {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final Long timeToAcceptOrder;
    private final OrderMapper orderMapper;


    @Scheduled(fixedRate = 10000)
    @TechnicalContextDev
    @Transactional
    public void sendTestOrderToWebsocket() {
        var order = createOrderForTest();
        order = orderRepository.save(order);
        log.info("Sending order to ts");
        orderService.sendNewOrdersNotification("1", orderMapper.mapToOrderDto(order));
    }

    public Order createOrderForTest() {
        var company = companyRepository.findById(1L).orElseThrow(
                () -> new IllegalStateException("Send new order - wrong company id"));
        HashMap<Long, Integer> quantityProductIdMap = new HashMap<>();
        quantityProductIdMap.put(1L, 2);
        quantityProductIdMap.put(2L, 1);
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
                .content(OrderContent.builder()
                        .quantityProductIdMap(quantityProductIdMap)
                        .build())
                .approvalDeadline(LocalDateTime.now().plusSeconds(25))
                //.approvalDeadline(LocalDateTime.now().plusMinutes(timeToAcceptOrder))
                .build();
    }

}
