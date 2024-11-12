package org.sunbong.allmart_api.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.common.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true, exclude = {"attachFiles"})
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String sku;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @ElementCollection
    @CollectionTable(name = "tbl_product_image")
    @BatchSize(size = 50)
    @Builder.Default
    private List<ProductImage> attachFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩하지 않고 필요할 때만 쿼리에서 실행
    @JoinColumn(name = "categoryid", nullable = false) // 외래 키 설정
    private Category category;

    public void addFile(String filename) {
        int nextOrd = attachFiles.stream()
                .mapToInt(ProductImage::getOrd)
                .max()
                .orElse(-1) + 1;  // 다음 ord 값 계산
        attachFiles.add(new ProductImage(filename, nextOrd));  // 고유한 ord로 추가
    }

    public void deleteFileByOrd(List<Integer> ordsToDelete) {
        this.attachFiles.removeIf(file -> ordsToDelete.contains(file.getOrd()));
    }
}

