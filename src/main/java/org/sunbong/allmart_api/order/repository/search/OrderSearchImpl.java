package org.sunbong.allmart_api.order.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.*;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.payment.domain.QPayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl() {
        super(OrderEntity.class);
    }

    @Override
    public PageResponseDTO<OrderListDTO> searchOrders(OrderStatus status, String customerId, PageRequestDTO pageRequestDTO) {
        log.info("searchOrders called with status: {}, customerId: {}", status, customerId);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("orderID").descending()
        );

        QOrderEntity orderEntity = QOrderEntity.orderEntity;
        QOrderItem orderItem = QOrderItem.orderItem;
        QPayment payment = QPayment.payment;

        // OrderItem을 기준으로 쿼리 작성
        JPQLQuery<OrderItem> query = from(orderItem)
                .leftJoin(orderItem.order, orderEntity) // OrderEntity와 조인
                .leftJoin(payment).on(payment.order.eq(orderEntity)); // Payment와 조인

        BooleanBuilder builder = new BooleanBuilder();

        // 주문 상태 필터링
        if (status != null) {
            builder.and(orderEntity.status.eq(status));
        }

        // 고객 ID 필터링
        if (customerId != null && !customerId.isEmpty()) {
            builder.and(orderEntity.customerId.eq(customerId));
        }

        query.where(builder).groupBy(orderItem.order);

        // Tuple을 사용하여 필요한 데이터 선택
        JPQLQuery<Tuple> tupleQuery = query.select(
                orderEntity,
                orderItem.count(),
                payment.amount.sum()
        );

        List<Tuple> results = getQuerydsl().applyPagination(pageable, tupleQuery).fetch();

        List<OrderListDTO> dtoList = results.stream().map(tuple -> {
            OrderEntity order = tuple.get(orderEntity);
            BigDecimal totalPayment = tuple.get(payment.amount.sum());

            return OrderListDTO.builder()
                    .orderId(order.getOrderID())
                    .customerId(order.getCustomerId())
                    .status(order.getStatus())
                    .totalAmount(totalPayment != null ? totalPayment : BigDecimal.ZERO)
                    .orderTime(order.getCreatedDate())
                    .build();
        }).collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<OrderListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

}