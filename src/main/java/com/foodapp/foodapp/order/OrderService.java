package com.foodapp.foodapp.order;

import com.foodapp.foodapp.company.CompanyRepository;
import com.foodapp.foodapp.order.request.ApproveNewIncomingOrderRequest;
import com.foodapp.foodapp.order.request.CreateOrderRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ContextProvider contextProvider;
    private final OrderValidator orderValidator;

    public static OrderDto mapToOrderDto(final Order order) {
        return OrderDto.builder()
                .name("Zamówienie" + order.getId())
                .companyId(order.getCompany().getId())
                .description(order.getDescription())
                .price(order.getPrice())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryTime(order.getDeliveryTime())
                .build();
    }

    public void sendNewOrdersNotification(final String userEmail, final OrderDto orderDto) {
        messagingTemplate.convertAndSendToUser(userEmail, "/order", orderDto);
    }

    @Transactional
    public void approveNewIncomingOrder(final ApproveNewIncomingOrderRequest request) {
        contextProvider.validateCompanyAccess(request.getCompanyId());
        var order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new SecurityException("Wrong action"));
        orderValidator.validateApprovingOrder(order, request.getCompanyId());
        order.setStatus(OrderStatus.IN_EXECUTION);
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
                .map(OrderService::mapToOrderDto)
                .collect(Collectors.toList());
    }
}
