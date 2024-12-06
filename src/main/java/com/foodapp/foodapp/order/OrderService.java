package com.foodapp.foodapp.order;

import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.order.request.ApproveNewIncomingOrderRequest;
import com.foodapp.foodapp.order.request.CreateOrderRequest;
import com.foodapp.foodapp.order.request.RejectNewIncomingOrderRequest;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.websocket.WebSocketEventSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ContextProvider contextProvider;
    private final OrderValidator orderValidator;
    private final WebSocketEventSender webSocketEventSender;

    @Transactional
    public void approveNewIncomingOrder(final ApproveNewIncomingOrderRequest request) {
        handleNewIncomingOrder(OrderStatus.IN_EXECUTION, request.getCompanyId(), request.getOrderId());
        webSocketEventSender.sendApprovedOrderWebSocketEvent(request.getOrderId(), request.getOrderReceivingTopicName());

        //todo pewnie trzeba strzelic czyms do glovo czy cos
    }

    @Transactional
    public void rejectNewIncomingOrder(final RejectNewIncomingOrderRequest request) {
        handleNewIncomingOrder(OrderStatus.REJECTED, request.getCompanyId(), request.getOrderId());
        webSocketEventSender.sendRejectedOrderWebSocketEvent(request.getOrderId(), request.getOrderReceivingTopicName());

        //todo pewnie trzeba strzelic czyms do glovo czy cos
    }

    private void handleNewIncomingOrder(final OrderStatus orderStatus,
                                        final Long companyId,
                                        final Long orderId) {
        contextProvider.validateCompanyAccess(companyId);
        var order = orderRepository.findById(orderId).orElseThrow(() -> new SecurityException("Wrong action"));
        orderValidator.validateIncomingOrder(order, companyId);
        order.setStatus(orderStatus);
        orderRepository.save(order);
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
                .map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public OrdersPagedResult getOrders(final SearchParams searchParams) {
        return orderRepository.searchOrders(searchParams);
    }
}
