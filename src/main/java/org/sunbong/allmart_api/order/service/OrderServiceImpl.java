package org.sunbong.allmart_api.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.OrderEntity;

import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.order.exception.OrderNotFoundException;
import org.sunbong.allmart_api.order.repository.OrderJpaRepository;
import org.sunbong.allmart_api.order.repository.search.OrderSearch;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderJpaRepository orderRepository;
    private final OrderSearch orderSearch;  // OrderSearch 의존성 추가

    @Override
    @Transactional(readOnly = true)
    public OrderListDTO getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<OrderListDTO> searchOrders(OrderStatus status, String customerId, PageRequestDTO pageRequestDTO) {

        return orderSearch.searchOrders(status, customerId, pageRequestDTO);
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderStatus newStatus) {

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        orderEntity.changeStatus(newStatus); // 상태 변경

        orderRepository.save(orderEntity);   // 변경 사항 저장
    }

    private OrderListDTO convertToDTO(OrderEntity orderEntity) {
        return OrderListDTO.builder()
                .orderId(orderEntity.getOrderID())
                .customerId(orderEntity.getCustomerId())
                .totalAmount(orderEntity.getTotalAmount())
                .status(orderEntity.getStatus())
                .orderTime(orderEntity.getCreatedDate())
                .build();
    }
}
