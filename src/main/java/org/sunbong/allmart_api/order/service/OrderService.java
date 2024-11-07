package org.sunbong.allmart_api.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.dto.OrderCheckDTO;
import org.sunbong.allmart_api.order.dto.OrderDTO;
import org.sunbong.allmart_api.order.dto.OrderItemDTO;
import org.sunbong.allmart_api.order.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public PageResponseDTO<OrderDTO> searchOrders(String status, String customerInfo, PageRequestDTO pageRequestDTO) {
        return orderRepository.searchOrders(status, customerInfo, pageRequestDTO);
    }

    // 주문 리스트

    // 주문 삭제

    // 주문 수정




}

