package org.sunbong.allmart_api.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.search.OrderSearch;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, OrderSearch {
}