package org.sunbong.allmart_api.order.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.customer.domain.QCustomer;
import org.sunbong.allmart_api.order.domain.OrderEntity;
import org.sunbong.allmart_api.order.domain.OrderStatus;
import org.sunbong.allmart_api.order.domain.QOrderEntity;
import org.sunbong.allmart_api.order.dto.OrderListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl() {
        super(OrderEntity.class);
    }
    @Override
    public PageResponseDTO<OrderListDTO> searchOrders(String status, String customerInfo, PageRequestDTO pageRequestDTO) {
        log.info("searchOrders called with status: {}, customerInfo: {}", status, customerInfo);

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        QOrderEntity orderEntity = QOrderEntity.orderEntity;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<OrderEntity> query = from(orderEntity)
                .leftJoin(orderEntity.customer, customer);

        BooleanBuilder builder = new BooleanBuilder();

        // 주문 상태 필터링
        if (status != null && !status.isEmpty()) {
            builder.and(orderEntity.status.eq(OrderStatus.valueOf(status.toUpperCase())));
        }

        // 고객명 또는 전화번호로 검색
        if (customerInfo != null && !customerInfo.isEmpty()) {
            BooleanBuilder customerCondition = new BooleanBuilder();
            customerCondition.or(customer.name.containsIgnoreCase(customerInfo));
            customerCondition.or(customer.phoneNumber.containsIgnoreCase(customerInfo));
            builder.and(customerCondition);
        }

        query.where(builder);

        // 정렬 설정
        OrderSpecifier<?> orderByStatus = orderEntity.status.asc();
        OrderSpecifier<?> orderByDate = orderEntity.createdDate.desc();

        query.orderBy(orderByStatus, orderByDate);

        // DTO 변환
        JPQLQuery<OrderListDTO> dtoQuery = query.select(
                Projections.bean(OrderListDTO.class,
                        orderEntity.orderID.as("orderId"),
                        customer.name.as("customerName"),
                        orderEntity.totalAmount,
                        orderEntity.status.stringValue().as("status"),
                        orderEntity.createdDate.as("orderDate")
                )
        );

        List<OrderListDTO> dtoList = dtoQuery.fetch();
        long total = dtoQuery.fetchCount();

        return new PageResponseDTO<>(dtoList, pageRequestDTO, total);
    }

}
