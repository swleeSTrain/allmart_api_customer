package org.sunbong.allmart_api.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "tbl_orderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemID;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private short quantity;

    // Order와의 Many-to-One 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderID", nullable = false)
    private OrderEntity order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    // Product와의 Many-to-One 관계 설정
}
