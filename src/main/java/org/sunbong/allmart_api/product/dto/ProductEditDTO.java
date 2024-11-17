package org.sunbong.allmart_api.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
    private Long categoryID;           // 변경할 카테고리 ID
}
