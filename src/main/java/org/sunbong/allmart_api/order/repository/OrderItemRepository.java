package org.sunbong.allmart_api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.order.domain.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_OrderID(Long orderId);
}