package com.foodapp.foodapp.forDevelopment.scheduler;


import com.foodapp.foodapp.administration.cache.CacheService;
import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.forDevelopment.TechnicalContextDev;
import com.foodapp.foodapp.order.*;
import com.foodapp.foodapp.order.sql.CustomOrderIdGenerator;
import com.foodapp.foodapp.order.sql.Order;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.rabbitMQ.RabbitMQSender;
import com.foodapp.foodapp.websocket.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class SchedulerForTestingService {
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
    public static int INDEX = 0;
    private final WebSocketService webSocketService;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final Long timeToAcceptOrder;
    private final Set<String> companyTopics = ConcurrentHashMap.newKeySet();
    private final CacheService cacheService;
    private final RabbitMQSender rabbitMQSender;
    private final CustomOrderIdGenerator customOrderIdGenerator;

    @Scheduled(fixedRate = 10000)
    public void xd() {
        System.out.println("sprawdzam cacheService");
        log.info(cacheService.getCompanyUsersCache().getEntries().toString());
        log.info(cacheService.getUsersConnectedToWebSocketCache().getEntries().toString());
    }
//
//    @Scheduled(fixedRate = 20000)
//    public void x2() {
//        cacheService.getCompanyUsersCache().get(1L);
//    }


    @Scheduled(fixedRate = 25000)
    @TechnicalContextDev
//    @Transactional w sumie to nie powinien byc transactional bo a noz sie wywali socket i przez to orer mi sie nie zapsize na bazie
    public void sendOrdersToMainTopic() {
//        System.out.println("companyTopics" + companyTopics);
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
        rabbitMQSender.sendMessage(order);
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
        var foodPrice = orderProducts.stream()
                .map(OrderProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var deliveryPrice = BigDecimal.valueOf(5);
        var order = Order.builder()
                .executionTime(LocalDateTime.now().plusMinutes(66))
                .deliveryAddress(Address.builder()
                        .country("Polska")
                        .postalCode("43-999")
                        .city("Katowice")
                        .street("Paderewskiego")
                        .streetNumber("22")
                        .build())
                .deliveryCode(UUID.randomUUID().toString().substring(0, 8))
                .orderType(INDEX % 2 != 0 ? OrderType.GLOVO : OrderType.PYSZNE_PL)
                .customerName("Maciek Franczak")
                .description(
                        "Poprosze osobno frytki i cole bez lodu. W razie problemow ze znalezeniem numry zostawic na portierni"
                )
                .status(OrderStatus.WAITING_FOR_ACCEPTANCE)
                .delivery(true)
                .foodPrice(foodPrice)
                .deliveryPrice(deliveryPrice)
                .totalPrice(foodPrice.add(deliveryPrice)).company(Company.builder()
                        .build())
                .company(company)
//                .approvalDeadline(LocalDateTime.now().plusSeconds(INDEX % 2 != 0 ? 25 : 10))
                .approvalDeadline(LocalDateTime.now().plusSeconds(90))
                .orderProducts(orderProducts)
                //.approvalDeadline(LocalDateTime.now().plusMinutes(timeToAcceptOrder))
                .build();
        Long nextDisplayableId = customOrderIdGenerator.generate(order);
        order.setDisplayableId(nextDisplayableId);
        return order;
    }

    public void updateTopicToSend(final Set<Long> companyIdsToAdd, final Set<Long> companyIdsToRemove) {
        //funkcja na potrzeby testowe
        var companiesToAdd = companyRepository.findAllById(companyIdsToAdd).stream()
                .map(Company::getWebSocketTopicName)
                .collect(Collectors.toList());
        companyTopics.addAll(companiesToAdd);

        var companiesToRemove = companyRepository.findAllById(companyIdsToRemove).stream()
                .map(Company::getWebSocketTopicName)
                .collect(Collectors.toList());
        companiesToRemove.forEach(companyTopics::remove);

        System.out.println("companyTopics");
        System.out.println(companyTopics);


    }

    public void removeTopicFromSending(final String webSocketTopicName) {
        companyTopics.remove(webSocketTopicName);
    }
}
