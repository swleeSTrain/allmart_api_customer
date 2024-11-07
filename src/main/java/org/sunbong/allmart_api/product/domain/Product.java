package org.sunbong.allmart_api.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
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
    private long productID;

    @Column(length = 100)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String sku;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ElementCollection
    @CollectionTable(name = "tbl_product_image")
    @BatchSize(size = 100)
    @Builder.Default
    private List<ProductImage> attachFiles = new ArrayList<>();

    public void addFile(String filename) {
        attachFiles.add(new ProductImage(filename, attachFiles.size()));
    }

    public void removeFile(String filename) {
        attachFiles.removeIf(image -> image.getImageURL().equals(filename));
    }

    // 파일 목록 업데이트 (파일 추가 및 삭제 처리)
    public void updateFiles(List<String> filesToAdd, List<String> filesToDelete) {
        filesToDelete.forEach(this::removeFile);
        filesToAdd.forEach(this::addFile);
    }
}

