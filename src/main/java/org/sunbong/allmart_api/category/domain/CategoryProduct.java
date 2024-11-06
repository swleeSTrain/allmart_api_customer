package org.sunbong.allmart_api.category.domain;

import jakarta.persistence.*;
import lombok.*;
import org.sunbong.allmart_api.product.domain.Product;

@Entity
@Table(name = "tbl_category_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"product","category"})
public class CategoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryProductID;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Category category;

}