package org.sunbong.allmart_api.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.OrderEntity;

import org.sunbong.allmart_api.order.domain.OrderItem;
import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.dto.OrderItemDTO;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.order.exception.OrderNotFoundException;
import org.sunbong.allmart_api.order.repository.OrderItemJpaRepository;
import org.sunbong.allmart_api.order.repository.OrderJpaRepository;
import org.sunbong.allmart_api.order.repository.search.OrderSearch;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.exception.ProductNotFoundException;
import org.sunbong.allmart_api.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;
    private final OrderItemJpaRepository orderItemRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional(readOnly = true)
    public OrderListDTO getOrderById(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // OrderItemRepository를 통해 OrderItem 리스트를 조회
        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderID(orderId);
        List<OrderItemDTO> orderItemDTOs = orderItems.stream()
                .map(item -> OrderItemDTO.builder()
                        .orderItemId(item.getOrderItemID())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .productId(item.getProduct().getProductID())
                        .productName(item.getProduct().getName())
                        .build())
                .collect(Collectors.toList());

        return OrderListDTO.builder()
                .orderId(order.getOrderID())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderTime(order.getCreatedDate())
                .orderItems(orderItemDTOs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<OrderListDTO> searchOrders(OrderStatus status, String customerId, PageRequestDTO pageRequestDTO) {

        return orderRepository.searchOrders(status, customerId, pageRequestDTO);
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderStatus newStatus) {

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        orderEntity.changeStatus(newStatus); // 상태 변경

        orderRepository.save(orderEntity);   // 변경 사항 저장
    }

    @Override
    public void createOrderFromVoice(String productName, int quantity, String userId) {
        // Product 조회
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productName));

        // OrderEntity 생성 (총 금액은 미리 계산하여 설정)
        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        OrderEntity order = OrderEntity.builder()
                .customerId(userId)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)  // 총 금액 미리 설정
                .build();

        // OrderEntity 저장
        orderRepository.save(order);

        // OrderItem 생성 및 저장
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .build();
        orderItemRepository.save(orderItem);
    }


    private BigDecimal calculateTotalAmount(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderOrderID(orderId);
        return orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

//    private OrderListDTO convertToDTO(OrderEntity orderEntity) {
//        return OrderListDTO.builder()
//                .orderId(orderEntity.getOrderID())
//                .customerId(orderEntity.getCustomerId())
//                .totalAmount(orderEntity.getTotalAmount())
//                .status(orderEntity.getStatus())
//                .orderTime(orderEntity.getCreatedDate())
//                .build();
//    }

}
