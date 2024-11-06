package com.foodapp.foodapp.order;

import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.order.request.ApproveNewIncomingOrderRequest;
import com.foodapp.foodapp.order.request.CreateOrderRequest;
import com.foodapp.foodapp.order.request.RejectNewIncomingOrderRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ContextProvider contextProvider;
    private final OrderValidator orderValidator;
    private final OrderMapper orderMapper;

    public void sendNewOrdersNotification(final String topicName, final OrderDto orderDto) {
        messagingTemplate.convertAndSendToUser(topicName, "/order", orderDto);
    }

    @Transactional
    public void approveNewIncomingOrder(final ApproveNewIncomingOrderRequest request) {
        contextProvider.validateCompanyAccess(request.getCompanyId());
        var order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new SecurityException("Wrong action"));
        orderValidator.validateIncomingOrder(order, request.getCompanyId());
        order.setStatus(OrderStatus.IN_EXECUTION);
        orderRepository.save(order);
        //todo pewnie trzeba strzelic czyms do glovo czy cos
    }

    @Transactional
    public void rejectNewIncomingOrder(final RejectNewIncomingOrderRequest request) {
        contextProvider.validateCompanyAccess(request.getCompanyId());
        var order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new SecurityException("Wrong action"));
        orderValidator.validateIncomingOrder(order, request.getCompanyId());
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        //todo pewnie trzeba strzelic czyms do glovo czy cos
    }

    public void saveOrder(final CreateOrderRequest request) {
        var company = companyRepository.findById(request.getOrder().getCompanyId())
                .orElseThrow(() -> new SecurityException("Comapny id not valid"));
        var order = Order.builder()
                .orderType(request.getOrder().getOrderType())
                .company(company)
                .description(request.getOrder().getDescription())
                .price(request.getOrder().getPrice())
                .status(request.getOrder().getStatus() == null ? OrderStatus.WAITING_FOR_ACCEPTANCE : request.getOrder().getStatus())
                .customerName(request.getOrder().getCustomerName())
                .deliveryAddress(request.getOrder().getDeliveryAddress())
                .deliveryTime(request.getOrder().getDeliveryTime())
                .build();
        orderRepository.save(order);
    }

    public List<OrderDto> getOrders(final Long companyId, final OrderStatus orderStatus) {
        return orderRepository.findByCompanyIdAndStatus(companyId, orderStatus).stream()
                .map(orderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public OrdersPagedResult getOrders(final SearchParams searchParams) {
        return orderRepository.searchOrders(searchParams);
    }
}
