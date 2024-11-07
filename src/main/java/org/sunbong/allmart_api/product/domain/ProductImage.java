package org.sunbong.allmart_api.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductImage {

    @Column(length = 255)
    private String imageURL;

    private int ord;
}
