package org.sunbong.allmart_api.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.delivery.domain.DeliveryEntity;
import org.sunbong.allmart_api.delivery.domain.DeliveryStatus;
import org.sunbong.allmart_api.delivery.dto.DeliveryDTO;
import org.sunbong.allmart_api.delivery.repository.DeliveryRepository;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.dto.OrderDTO;
import org.sunbong.allmart_api.order.repository.OrderJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final OrderJpaRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * 2시간 동안 COMPLETED 상태의 주문을 고객 ID별로 묶어 배달 생성
     */
    public List<DeliveryDTO> processOrdersForDelivery(LocalDateTime startTime, LocalDateTime endTime) {
        // COMPLETED 상태의 주문 조회
        List<OrderEntity> completedOrders = orderRepository.findByStatusAndCreatedDateBetween(OrderStatus.COMPLETED, startTime, endTime);

        // 고객 ID별로 주문 그룹화
        Map<String, List<OrderEntity>> ordersGroupedByCustomer = completedOrders.stream()
                .collect(Collectors.groupingBy(OrderEntity::getCustomerId));

        // 고객별로 배달 생성 및 주문 연결
        return ordersGroupedByCustomer.entrySet().stream()
                .map(entry -> {
                    String customerId = entry.getKey();
                    List<OrderEntity> orders = entry.getValue();

                    // 배달 생성
                    DeliveryEntity delivery = DeliveryEntity.builder()
                            .deliveryTime(LocalDateTime.now())
                            .status(DeliveryStatus.PENDING)
                            .build();
                    deliveryRepository.save(delivery);

                    // 주문에 배달 연결
                    orders.forEach(order -> {
                        order.assignDelivery(delivery);
                        orderRepository.save(order); // 주문 업데이트
                    });

                    // DeliveryEntity -> DeliveryDTO 변환
                    return DeliveryDTO.builder()
                            .deliveryId(delivery.getDeliveryID())
                            .deliveryTime(delivery.getDeliveryTime())
                            .status(delivery.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 배달 ID에 포함된 주문 조회
     */
    public List<OrderDTO> getOrdersByDeliveryId(Long deliveryId) {
        List<OrderEntity> orders = orderRepository.findByDeliveryDeliveryID(deliveryId);

        return orders.stream()
                .map(order -> OrderDTO.builder()
                        .orderId(order.getOrderID())
                        .customerId(order.getCustomerId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus().name())
                        .orderTime(order.getCreatedDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 배달 상태 변경
     */
    public DeliveryDTO changeDeliveryStatus(Long deliveryId, String newStatus) {
        DeliveryEntity delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for ID: " + deliveryId));

        try {
            DeliveryStatus status = DeliveryStatus.valueOf(newStatus.toUpperCase());
            delivery = delivery.toBuilder().status(status).build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid delivery status: " + newStatus);
        }

        deliveryRepository.save(delivery);

        return DeliveryDTO.builder()
                .deliveryId(delivery.getDeliveryID())
                .deliveryTime(delivery.getDeliveryTime())
                .status(delivery.getStatus().name())
                .build();
    }
}


