package org.sunbong.allmart_api.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.sunbong.allmart_api.product.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEditDTO {

    private String name;
    private String sku;
    private BigDecimal price;
    private List<MultipartFile> files;
    private List<String> filesToDelete; // 삭제할 파일 이름 목록
    private String categoryName; // 카테고리 이름 추가

    public Product toUpdatedProduct(Product existingProduct) {

        // 기존 상품을 Builder로 업데이트
        Product updatedProduct = existingProduct.toBuilder()
                .name(this.name != null ? this.name : existingProduct.getName())
                .sku(this.sku != null ? this.sku : existingProduct.getSku())
                .price(this.price != null ? this.price : existingProduct.getPrice())
                .build();

        // 파일 삭제 처리
        if (this.filesToDelete != null && !this.filesToDelete.isEmpty()) {
            updatedProduct.updateFiles(new ArrayList<>(), this.filesToDelete);
        }

        // 새로운 파일들이 있을 경우 파일 추가
        if (this.files != null && !this.files.isEmpty()) {
            List<String> newFiles = new ArrayList<>();
            this.files.forEach(file -> newFiles.add(file.getOriginalFilename()));
            updatedProduct.updateFiles(newFiles, new ArrayList<>());
        }

        return updatedProduct;
    }
}
