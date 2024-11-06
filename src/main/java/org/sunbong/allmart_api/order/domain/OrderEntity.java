package org.sunbong.allmart_api.order.domain;


import jakarta.persistence.*;
import lombok.*;
import org.sunbong.allmart_api.common.domain.BaseEntity;
import org.sunbong.allmart_api.customer.domain.Customer;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
@Table(name = "tbl_order",
        indexes = {@Index(name = "idx_orderDate", columnList = "createdDate"),// 주문일 인덱스 설정
                @Index(name = "idx_customerID", columnList = "customerID")}  // 고객 ID 인덱스 추가
)
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID", nullable = false)
    private Customer customer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private int notification;

}
