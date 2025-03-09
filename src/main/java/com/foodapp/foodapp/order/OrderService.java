package com.foodapp.foodapp.order;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.Sort;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.order.request.*;
import com.foodapp.foodapp.order.response.CreateOrderRequestResponse;
import com.foodapp.foodapp.order.response.ModifyOrderResponse;
import com.foodapp.foodapp.order.sql.Order;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductRepository;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.websocket.EventMapper;
import com.foodapp.foodapp.websocket.WebSocketEventSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final ContextProvider contextProvider;
    private final OrderValidator orderValidator;
    private final WebSocketEventSender webSocketEventSender;
    private final OrderMapper orderMapper;
    private final OrderProductRepository orderProductRepository;

    public void approveNewIncomingOrder(final ApproveNewIncomingOrderRequest request) {
        var topicName = handleWaitingForAcceptanceOrder(OrderStatus.IN_EXECUTION, request.getCompanyId(), request.getOrderId());
        var event = EventMapper.createApprovedOrderWebSocketEvent(request.getOrderId());
        webSocketEventSender.fire(event, topicName);

        //todo pewnie trzeba strzelic czyms do glovo czy cos
    }

    public void rejectNewIncomingOrder(final RejectNewIncomingOrderRequest request) {
        var topicName = handleWaitingForAcceptanceOrder(OrderStatus.REJECTED, request.getCompanyId(), request.getOrderId());
        var event = EventMapper.createRejectedOrderWebSocketEvent(request.getOrderId());
        webSocketEventSender.fire(event, topicName);

        //todo pewnie trzeba strzelic czyms do glovo czy cos
    }

    private String handleWaitingForAcceptanceOrder(final OrderStatus orderStatus,
                                                   final Long companyId,
                                                   final Long orderId) {
        contextProvider.validateCompanyAccessWithHolding(List.of(companyId));
        var order = orderRepository.findById(orderId).orElseThrow(() -> new SecurityException("Wrong action"));
        orderValidator.validateWaitingForAcceptanceOrder(order, companyId);
        order.setStatus(orderStatus);
        orderRepository.save(order);
        return order.getCompany().getWebSocketTopicName();
    }

    @Transactional
    public CreateOrderRequestResponse saveOrder(final CreateOrderRequest request, final Long companyId)
            throws UnsupportedEncodingException {
        contextProvider.validateCompanyAccess(List.of(companyId));
        orderValidator.validateOrderSave(request.getOrder(), companyId);
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        var order = orderMapper.mapToOrder(request.getOrder(), company, Optional.empty());
        order = orderRepository.save(order);
        return OrderMapper.createOrderResponse(request.getOrder(), order.getDisplayableId(), request.isPrintViaBluetooth());
    }

    @Transactional
    public ModifyOrderResponse modifyOrder(final ModifyOrderRequest request, final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var modifiedOrder = orderValidator.validateOrderModifyAndReturn(request.getOrder(), companyId, request.getModifiedOrderIf());
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        var order = orderMapper.mapToOrder(request.getOrder(), company, Optional.of(modifiedOrder));
        modifiedOrder.setStatus(OrderStatus.MODIFIED);
        orderRepository.save(order);
        orderRepository.save(modifiedOrder);
        return ModifyOrderResponse.builder()
                .displayableOrderId(order.getDisplayableId())
                .build();
    }

    @Transactional
    public void finalizeOrder(final FinalizeOrderRequest request, final Long companyId, Long orderId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var order = orderValidator.validateOrderFinalizationAndReturn(orderId, companyId);
        order.setStatus(OrderStatus.EXECUTED);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setTakeaway(request.isTakeaway());
        order.setTotalPrice(request.getNewTotalPrice());
        orderRepository.save(order);
    }

    public List<OrderDto> getOrders(final Set<Long> companyIds, final List<OrderStatus> orderStatuses, final List<Sort> sorts) {
        List<Order> orders =
                CollectionUtils.isEmpty(sorts) ? orderRepository.findByCompany_IdInAndStatusIn(companyIds, orderStatuses) :
                        orderRepository.findByCompany_IdInAndStatusIn(companyIds, orderStatuses, CommonMapper.toSort(sorts));
        return orders.stream()
                .map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public OrdersPagedResult getOrders(final GetOrdersForCompanyRequest request) {
        var contextCompanyIds = contextProvider.getCompanyIdsWithHolding(List.of(request.getValidatableCompanyId()));
        var companyIds = CollectionUtils.isEmpty(request.getCompanyIds()) ? contextCompanyIds : request.getCompanyIds();
        var searchParams =
                CommonMapper.mapToSearchParams(request, companyIds);
        return orderRepository.searchOrders(searchParams);
    }
}
