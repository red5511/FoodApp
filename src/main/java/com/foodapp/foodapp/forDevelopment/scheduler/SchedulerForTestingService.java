package com.foodapp.foodapp.forDevelopment.scheduler;


import com.foodapp.foodapp.administration.company.Company;
import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.order.*;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.websocket.WebSocketService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Slf4j
public class SchedulerForTestingService {
    private final WebSocketService webSocketService;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final Long timeToAcceptOrder;
    private final Set<String> companyTopics;


    @Scheduled(fixedRate = 10000)
    @TechnicalContextDev
    @Transactional
    public void sendOrdersToMainTopic() {
        companyTopics.forEach(this::handleSending);
    }

    public void handleSending(final String topicName) {
        var orderProducts = createOrderProductForTest();
        var order = createOrderForTest(orderProducts);
        final Order finalOrder = order;
        orderProducts.forEach(el -> el.setOrder(finalOrder));
        order = orderRepository.save(order);
        log.info("Sending order to ts");
        webSocketService.sendNewOrderToTopic(topicName, OrderMapper.mapToOrderDto(order));
    }

    private List<OrderProduct> createOrderProductForTest() {
        var products = productRepository.findAllById(List.of(1L, 2L));
        if (products.size() == 0) {
            throw new IllegalStateException("Send new order - wrong product ids");
        }
        List<OrderProduct> orderProducts = new ArrayList<>();
        var multiplies = List.of(2, 3);
        for (int i = 0; i < products.size(); i++) {
            orderProducts.add(OrderProduct.builder()
                    .quantity(multiplies.get(i))
                    .price(products.get(i).getPrice().multiply(BigDecimal.valueOf(multiplies.get(i))))
                    .product(products.get(i))
                    .build());
        }
        return orderProducts;
    }

    public Order createOrderForTest(final List<OrderProduct> orderProducts) {
        var price = orderProducts.stream()
                .map(OrderProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var company = companyRepository.findById(1L).orElseThrow(
                () -> new IllegalStateException("Send new order - wrong company id"));
        return Order.builder()
                .deliveryTime(LocalDateTime.now())
                .deliveryAddress("Sikorskiego 43")
                .orderType(OrderType.GLOVO)
                .customerName("Maciek Franczak")
                .description(
                        "Poprosze osobno frytki i cole bez lodu. W razie problemow ze znalezeniem numry zostawic na portierni"
                )
                .status(OrderStatus.WAITING_FOR_ACCEPTANCE)
                .price(price)
                .company(Company.builder()
                        .build())
                .company(company)
                .approvalDeadline(LocalDateTime.now().plusSeconds(25))
                .orderProducts(orderProducts)
                //.approvalDeadline(LocalDateTime.now().plusMinutes(timeToAcceptOrder))
                .build();
    }

    public void addNewTopicToSend(final String webSocketTopicName) {
        companyTopics.add(webSocketTopicName);
    }
}
