package org.sunbong.allmart_api.order.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.order.domain.*;
import org.sunbong.allmart_api.order.dto.OrderListDTO;
import org.sunbong.allmart_api.order.dto.OrderItemDTO;
import org.sunbong.allmart_api.payment.domain.Payment;
import org.sunbong.allmart_api.payment.domain.QPayment;
import org.sunbong.allmart_api.payment.dto.OrderPaymentDTO;

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

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        QOrderEntity orderEntity = QOrderEntity.orderEntity;
        QOrderItem orderItem = QOrderItem.orderItem;
        QPayment payment = QPayment.payment;

        // OrderItem을 기준으로 OrderEntity와 Payment를 leftJoin
        JPQLQuery<OrderItem> query = from(orderItem)
                .leftJoin(orderItem.order, orderEntity) // OrderEntity와의 left join
                .leftJoin(payment).on(payment.order.eq(orderEntity)); // Payment와의 left join

        BooleanBuilder builder = new BooleanBuilder();

        // 주문 상태 필터링
        if (status != null) {
            builder.and(orderEntity.status.eq(status));
        }

        // 고객 ID 검색 조건 (String 타입의 customerId 직접 사용)
        if (customerId != null && !customerId.isEmpty()) {
            builder.and(orderEntity.customerId.eq(customerId));
        }

        query.where(builder);

        // applyPagination을 사용해 페이징 및 정렬 적용
        JPQLQuery<OrderItem> pageableQuery = getQuerydsl().applyPagination(pageable, query);

        // 엔티티 조회 및 DTO 변환
        List<OrderListDTO> dtoList = pageableQuery.fetch().stream()
                .collect(Collectors.groupingBy(OrderItem::getOrder)) // OrderEntity별로 그룹화
                .entrySet().stream()
                .map(entry -> {
                    OrderEntity order = entry.getKey();
                    List<OrderItemDTO> orderItems = entry.getValue().stream()
                            .map(item -> OrderItemDTO.builder()
                                    .orderItemId(item.getOrderItemID())
                                    .quantity(item.getQuantity())
                                    .unitPrice(item.getUnitPrice())
                                    .productId(item.getProduct().getProductID())
                                    .productName(item.getProduct().getName())
                                    .build())
                            .collect(Collectors.toList());

                    // Payment 정보 조회 및 변환
                    Payment paymentEntity = pageableQuery.select(payment)  // payment 데이터 가져오기
                            .from(payment)
                            .where(payment.order.eq(order))
                            .fetchOne();

                    OrderPaymentDTO paymentDTO = null;
                    if (paymentEntity != null) {
                        paymentDTO = OrderPaymentDTO.builder()
                                .paymentID(paymentEntity.getPaymentID())
                                .method(paymentEntity.getMethod())
                                .amount(paymentEntity.getAmount())
                                .completed(paymentEntity.getCompleted())
                                .build();
                    }

                    // OrderEntity -> OrderListDTO 변환
                    return OrderListDTO.builder()
                            .orderId(order.getOrderID())
                            .customerId(order.getCustomerId())
                            .status(order.getStatus())
                            .totalAmount(order.getTotalAmount())
                            .orderTime(order.getCreatedDate())
                            .orderItems(orderItems)
                            .payment(paymentDTO) // 결제 정보 추가
                            .build();
                })
                .collect(Collectors.toList());

        long total = query.fetchCount();

        return new PageResponseDTO<>(dtoList, pageRequestDTO, total);
    }
}