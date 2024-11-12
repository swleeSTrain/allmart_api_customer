package org.sunbong.allmart_api.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.domain.OrderItem;
import org.sunbong.allmart_api.order.dto.OrderDetailDTO;
import org.sunbong.allmart_api.order.dto.OrderItemDTO;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.order.repository.OrderItemRepository;
import org.sunbong.allmart_api.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 주문 목록 조회
    public PageResponseDTO<OrderListDTO> getOrderList(String status, String customerInfo, PageRequestDTO pageRequestDTO) {
        return orderRepository.searchOrders(status, customerInfo, pageRequestDTO);
    }

    // 주문 상세 조회
    public OrderDetailDTO getOrderDetails(Long orderId) {
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(orderId);

        if (orderEntityOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        OrderEntity orderEntity = orderEntityOptional.get();

        // OrderItem 수량 및 총 가격 계산
        List<OrderItem> orderItems = orderItemRepository.findByOrder_OrderID(orderId);
        int orderQuantity = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderDetailDTO.builder()
                .orderStatus(orderEntity.getStatus().name())
                .orderQuantity(orderQuantity)  // OrderItem 수량 합산
                .customerId(orderEntity.getCustomer().getCustomerID())
                .paymentMethod(orderEntity.getPayment().getMethod().name())
                .totalAmount(totalAmount)
                .orderDate(orderEntity.getCreatedDate())
                .build();
    }
}
