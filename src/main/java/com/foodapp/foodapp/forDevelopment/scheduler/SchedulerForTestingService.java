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
import java.util.Arrays;
import java.util.HashSet;

@AllArgsConstructor
@Slf4j
public class SchedulerForTestingService {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final Long timeToAcceptOrder;

    @Scheduled(fixedRate = 10000)
    @TechnicalContextDev
    @Transactional
    public void sendTestOrderToWebsocket() {
        var order = createOrderForTest();
        order = orderRepository.save(order);
        log.info("Sending order to ts");
        orderService.sendNewOrdersNotification("1", OrderMapper.mapToOrderDto(order));
    }

    public Order createOrderForTest() {
        var company = companyRepository.findById(1L).orElseThrow(
                () -> new IllegalStateException("Send new order - wrong company id"));
        var product =
                productRepository.findById(1L).orElseThrow(
                        () -> new IllegalStateException("Send new order - wrong product id"));
        var product2 =
                productRepository.findById(2L).orElseThrow(
                        () -> new IllegalStateException("Send new order - wrong product id"));
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
                .products(new HashSet<>(Arrays.asList(product, product2, product2)))
                .approvalDeadline(LocalDateTime.now().plusSeconds(25))
                //.approvalDeadline(LocalDateTime.now().plusMinutes(timeToAcceptOrder))
                .build();
    }

}
