package org.sunbong.allmart_api.product.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.domain.QProduct;
import org.sunbong.allmart_api.product.dto.ProductListDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductListDTO> productList(PageRequestDTO pageRequestDTO) {

        log.info("-------------------list with search-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("productID").descending()
        );

        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        // 키워드 및 타입에 대한 검색 조건 추가
        if (keyword != null && type != null) {
            if (type.contains("name")) {
                builder.or(product.name.containsIgnoreCase(keyword));
            }
            if (type.contains("sku")) {
                builder.or(product.sku.containsIgnoreCase(keyword));
            }
        }

        JPQLQuery<Product> query = from(product)
                .where(builder);

        // 총 개수 계산 및 페이징 적용
        getQuerydsl().applyPagination(pageable, query);

        // Product 엔티티를 기반으로 Tuple 쿼리 생성
        List<Product> productList = query.fetch();
        long total = query.fetchCount();

        log.info(productList);

        log.info("======================================================");

        // ProductListDTO 생성
        List<ProductListDTO> dtoList = productList.stream()
                .map(prod -> {
                    // 파일 이름에 "s_"를 추가하여 썸네일 이미지 설정
                    List<String> modifiedAttachFiles = prod.getAttachFiles().stream()
                            .map(filename -> "s_" + filename.getImageURL())
                            .collect(Collectors.toList());

                    // 첫 번째 파일을 썸네일 이미지로 사용
                    String thumbnailImage = modifiedAttachFiles.isEmpty() ? null : modifiedAttachFiles.get(0);

                    return ProductListDTO.builder()
                            .productID(prod.getProductID())
                            .name(prod.getName())
                            .sku(prod.getSku())
                            .price(prod.getPrice())
                            .thumbnailImage(thumbnailImage)
                            .build();
                })
                .collect(Collectors.toList());

        log.info("ProductListDTO 생성 완료");
        log.info("========================================");

        // 결과 반환
        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
