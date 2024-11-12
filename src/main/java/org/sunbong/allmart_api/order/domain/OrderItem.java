package org.sunbong.allmart_api.order.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sunbong.allmart_api.product.domain.Product;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = {"order","product"})
@Table(
        name = "tbl_orderItem",
        indexes = {
                @Index(name = "idx_orderID", columnList = "orderID"),      // 주문 ID 인덱스
                @Index(name = "idx_productID", columnList = "productID")   // 제품 ID 인덱스
        }
)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemID;

    @Column(columnDefinition = "SMALLINT", nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;


    // Order와의 Many-to-One 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderID", nullable = false)
    private OrderEntity order;

    // Product와의 Many-to-One 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productID", nullable = false)
    private Product product;

}
