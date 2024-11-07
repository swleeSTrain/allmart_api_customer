package org.sunbong.allmart_api.product.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.category.domain.CategoryProduct;
import org.sunbong.allmart_api.category.domain.QCategory;
import org.sunbong.allmart_api.category.domain.QCategoryProduct;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.domain.QProduct;
import org.sunbong.allmart_api.product.domain.QProductImage;
import org.sunbong.allmart_api.product.dto.ProductListDTO;
import org.sunbong.allmart_api.product.dto.ProductReadDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("-------------------list----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("productID").descending()
        );

        QProduct product = QProduct.product;
        QProductImage attachFile = QProductImage.productImage;

        BooleanBuilder builder = new BooleanBuilder();
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        if (keyword != null && type != null) {
            if (type.contains("name")) {
                builder.or(product.name.containsIgnoreCase(keyword));
            }
            if (type.contains("sku")) {
                builder.or(product.sku.containsIgnoreCase(keyword));
            }
        }

        // 엔티티 조회
        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles, attachFile).fetchJoin()
                .where(builder)
                .groupBy(product);

        // 페이징 적용
        getQuerydsl().applyPagination(pageable, query);

        List<Product> productList = query.fetch();
        long total = query.fetchCount();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(prod -> {
                    String thumbnailImage = prod.getAttachFiles().isEmpty()
                            ? null
                            : "s_" + prod.getAttachFiles().get(0).getImageURL();
                    return ProductListDTO.builder()
                            .productID(prod.getProductID())
                            .name(prod.getName())
                            .sku(prod.getSku())
                            .price(prod.getPrice())
                            .thumbnailImage(thumbnailImage)
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    public ProductReadDTO readById(Long productID) {

        log.info("-------------------read----------");

        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
        QProduct product = QProduct.product;
        QProductImage attachFile = QProductImage.productImage;
        QCategory category = QCategory.category;

        // CategoryProduct를 기준으로 Product, attachFiles, Category를 모두 leftJoin으로 병합
        CategoryProduct result = from(categoryProduct)
                .leftJoin(categoryProduct.product, product).fetchJoin() // Product 병합
                .leftJoin(product.attachFiles, attachFile).fetchJoin() // Product의 첨부 파일 병합
                .leftJoin(categoryProduct.category, category).fetchJoin() // Category 병합
                .where(product.productID.eq(productID))
                .fetchOne();

        if (result == null) {
            return null;
        }

        // 파일 이름 리스트 생성
        List<String> attachFiles = result.getProduct().getAttachFiles().stream()
                .map(file -> file.getImageURL())
                .collect(Collectors.toList());

        // Category 이름 가져오기
        String categoryName = result.getCategory().getName();

        return ProductReadDTO.builder()
                .productID(result.getProduct().getProductID())
                .name(result.getProduct().getName())
                .sku(result.getProduct().getSku())
                .price(result.getProduct().getPrice())
                .attachFiles(attachFiles)
                .categoryName(categoryName) // 카테고리 이름 추가
                .createdDate(result.getProduct().getCreatedDate())
                .modifiedDate(result.getProduct().getModifiedDate())
                .build();
    }





}
