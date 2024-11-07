package org.sunbong.allmart_api.order.repository.search;

import com.querydsl.core.BooleanBuilder;
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
import org.sunbong.allmart_api.order.dto.OrderDTO;

import java.util.Collections;
import java.util.List;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;

@Log4j2
public class OrderSearchImpl extends QuerydslRepositorySupport implements  OrderSearch {

    public OrderSearchImpl() {super(OrderEntity.class);}

    @Override
    public PageResponseDTO<OrderDTO> searchOrders(String status, String customerInfo, PageRequestDTO pageRequestDTO) {

        log.info("searchOrders called with status: {}, customerInfo: {}", status, customerInfo);

        // Pageable 객체 생성 (정렬 설정 없음)
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize()
        );
        log.info("Pageable created with page: {}, size: {}", pageRequestDTO.getPage(), pageRequestDTO.getSize());

        QOrderEntity orderEntity = QOrderEntity.orderEntity;
        QCustomer customer = QCustomer.customer;

        JPQLQuery<OrderEntity> query = from(orderEntity)
                .leftJoin(orderEntity.customer, customer);

        // 검색 조건 설정
        BooleanBuilder builder = new BooleanBuilder();

        // 주문 상태 필터링 추가 (status가 전달된 경우)
        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                builder.and(orderEntity.status.eq(orderStatus));
                log.info("Added order status filter: {}", orderStatus);
            } catch (IllegalArgumentException e) {
                log.error("Invalid status value: {}", status);
                throw new IllegalArgumentException("Invalid status value: " + status);
            }
        }

        // 고객명 또는 전화번호로 검색 가능하도록 조건 추가
        if (customerInfo != null && !customerInfo.isEmpty()) {
            BooleanBuilder customerCondition = new BooleanBuilder();
            customerCondition.or(customer.name.containsIgnoreCase(customerInfo)); // 고객명 검색
            customerCondition.or(customer.phoneNumber.containsIgnoreCase(customerInfo)); // 고객 전화번호 검색
            builder.and(customerCondition);
            log.info("Added customer info filter with name or phone number containing: {}", customerInfo);
        }

        query.where(builder);

        // 상태별 우선순위 설정 (필터링된 상태에 대해 정렬 적용)
        OrderSpecifier<?> orderPending = Expressions.booleanTemplate("case when {0} = {1} then 0 else 1 end",
                orderEntity.status, OrderStatus.PENDING).asc();
        OrderSpecifier<?> orderByCreatedDate = orderEntity.createdDate.asc();
        OrderSpecifier<?> orderByModifiedDate = orderEntity.modifiedDate.desc();

        query.orderBy(orderPending, orderByCreatedDate, orderByModifiedDate);
        log.info("Query ordered by status, createdDate, modifiedDate");

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);
        log.info("Pagination applied with pageable: {}", pageable);

        // DTO로 변환하여 조회
        JPQLQuery<OrderDTO> dtoJPQLQuery = query.select(
                Projections.bean(OrderDTO.class,
                        orderEntity.orderID,
                        customer.name.as("customerName"),
                        orderEntity.totalAmount,
                        orderEntity.status.stringValue().as("status"),
                        orderEntity.createdDate.as("orderDate")
                )
        );

        List<OrderDTO> dtoList = dtoJPQLQuery.fetch();
        log.info("Fetched {} results", dtoList.size());

        if (dtoList.isEmpty()) {
            log.info("No results found, returning empty PageResponseDTO");
            return new PageResponseDTO<>(Collections.emptyList(), pageRequestDTO, 0);
        }

        // 전체 데이터 개수 계산
        long total = dtoJPQLQuery.fetchCount();
        log.info("Total count fetched: {}", total);

        // PageResponseDTO 생성 및 반환
        return PageResponseDTO.<OrderDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


}
