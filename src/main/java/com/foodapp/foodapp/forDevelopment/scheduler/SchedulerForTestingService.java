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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Slf4j
public class SchedulerForTestingService {
    public static int INDEX = 0;
    public static final Map<String, List<Long>> PRODUCTS_IDS_BY_COMPANY_TOPIC_NAME = Map.of(
            "Topic1", List.of(1L, 2L),
            "Topic2", List.of(3L, 4L),
            "Topic3", List.of(5L)
    );
    public static final Map<String, Long> COMPANY_ID_BY_COMPANY_TOPIC_NAME = Map.of(
            "Topic1", 1L,
            "Topic2", 2L,
            "Topic3", 3L
    );
    private final WebSocketService webSocketService;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final Long timeToAcceptOrder;
    private final Set<String> companyTopics = ConcurrentHashMap.newKeySet();



    @Scheduled(fixedRate = 10000)
    @TechnicalContextDev
    @Transactional
    public void sendOrdersToMainTopic() {
        System.out.println("companyTopics"+companyTopics);
        companyTopics.forEach(this::handleSending);
    }

    public void handleSending(final String topicName) {
        var orderProducts = createOrderProductForTest(PRODUCTS_IDS_BY_COMPANY_TOPIC_NAME.get(topicName));
        var company = companyRepository.findById(COMPANY_ID_BY_COMPANY_TOPIC_NAME.get(topicName)).orElseThrow(
                () -> new IllegalStateException("Send new order - wrong company id"));
        var order = createOrderForTest(orderProducts, company);
        //todo no tutaj bedzie pytanie skad glovo ma wiedziec jakie id ma moja firma xd
        final Order finalOrder = order;
        orderProducts.forEach(el -> el.setOrder(finalOrder));
        order = orderRepository.save(order);
        log.info("Sending order to ts");
        webSocketService.sendNewOrderToTopic(topicName, OrderMapper.mapToOrderDto(order), company.getId());
    }

    private List<OrderProduct> createOrderProductForTest(List<Long> productsIds) {
        var products = productRepository.findAllById(productsIds);
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

    public Order createOrderForTest(final List<OrderProduct> orderProducts, final Company company) {
        INDEX += 1;
        var price = orderProducts.stream()
                .map(OrderProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
                .approvalDeadline(LocalDateTime.now().plusSeconds(INDEX % 2 != 0 ? 25 : 10))
                .orderProducts(orderProducts)
                //.approvalDeadline(LocalDateTime.now().plusMinutes(timeToAcceptOrder))
                .build();
    }

    public void addNewTopicToSend(final String webSocketTopicName) {
        companyTopics.add(webSocketTopicName);
    }

    public void removeTopicFromSending(final String webSocketTopicName) {
        companyTopics.remove(webSocketTopicName);
    }
}
