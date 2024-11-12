package org.sunbong.allmart_api.order.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.repository.search.OrderSearch;
@Primary
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long>, OrderSearch {
}