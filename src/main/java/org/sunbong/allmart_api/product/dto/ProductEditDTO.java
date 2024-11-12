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
    private List<MultipartFile> files; // 새로운 파일 목록
    private List<Integer> ordsToDelete; // 삭제할 파일 ord 목록으로 변경
    private Long categoryID;

}
