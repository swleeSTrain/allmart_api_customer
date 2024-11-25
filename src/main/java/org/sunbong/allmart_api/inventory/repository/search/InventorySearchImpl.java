package org.sunbong.allmart_api.inventory.repository.search;

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
import org.sunbong.allmart_api.inventory.domain.Inventory;
import org.sunbong.allmart_api.inventory.domain.QInventory;
import org.sunbong.allmart_api.inventory.dto.InventoryListDTO;
import org.sunbong.allmart_api.product.domain.QProduct;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class InventorySearchImpl extends QuerydslRepositorySupport implements InventorySearch {

    public InventorySearchImpl() {
        super(Inventory.class);
    }

    @Override
    public PageResponseDTO<InventoryListDTO> searchInventory(PageRequestDTO pageRequestDTO) {
        log.info("searchInventory called with keyword: {}, type: {}",
                pageRequestDTO.getKeyword(), pageRequestDTO.getType());

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("inventoryID").descending()
        );

        QInventory inventory = QInventory.inventory;
        QProduct product = QProduct.product;

        JPQLQuery<Inventory> query = from(inventory)
                .leftJoin(inventory.product, product);

        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건: SKU 또는 상품명
        if (pageRequestDTO.getKeyword() != null && !pageRequestDTO.getKeyword().isEmpty()) {
            if ("sku".equalsIgnoreCase(pageRequestDTO.getType())) {
                builder.and(product.sku.containsIgnoreCase(pageRequestDTO.getKeyword()));
            } else if ("name".equalsIgnoreCase(pageRequestDTO.getType())) {
                builder.and(product.name.containsIgnoreCase(pageRequestDTO.getKeyword()));
            }
        }

        // 추가 필터 조건
        if (pageRequestDTO.getInventoryID() != null) {
            builder.and(inventory.inventoryID.eq(pageRequestDTO.getInventoryID()));
        }

        query.where(builder);

        // 데이터 페이징
        JPQLQuery<Tuple> tupleQuery = query.select(
                inventory.inventoryID,
                product.name,
                product.sku,
                inventory.quantity,
                inventory.inStock
        );

        List<Tuple> results = getQuerydsl().applyPagination(pageable, tupleQuery).fetch();

        // DTO로 변환
        List<InventoryListDTO> dtoList = results.stream().map(tuple -> InventoryListDTO.builder()
                .inventoryID(tuple.get(inventory.inventoryID))
                .productName(tuple.get(product.name))
                .sku(tuple.get(product.sku))
                .quantity(tuple.get(inventory.quantity))
                .inStock(tuple.get(inventory.inStock))
                .build()
        ).collect(Collectors.toList());

        // 전체 데이터 개수
        long total = query.fetchCount();

        return PageResponseDTO.<InventoryListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

}
