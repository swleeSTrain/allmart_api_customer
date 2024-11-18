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
import org.sunbong.allmart_api.inventory.dto.InventoryDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class InventorySearchImpl extends QuerydslRepositorySupport implements InventorySearch {

    public InventorySearchImpl() {
        super(Inventory.class);
    }

    @Override
    public PageResponseDTO<InventoryDTO> searchInventory(PageRequestDTO pageRequestDTO) {
        log.info("searchInventory called with keyword: {}, type: {}",
                pageRequestDTO.getKeyword(), pageRequestDTO.getType());

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("inventoryID").ascending()
        );

        QInventory inventory = QInventory.inventory;

        JPQLQuery<Inventory> query = from(inventory);

        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건: SKU 또는 기타 키워드
        if (pageRequestDTO.getKeyword() != null && !pageRequestDTO.getKeyword().isEmpty()) {
            if ("sku".equalsIgnoreCase(pageRequestDTO.getType())) {
                builder.and(inventory.sku.containsIgnoreCase(pageRequestDTO.getKeyword()));
            } else {
                builder.and(inventory.sku.containsIgnoreCase(pageRequestDTO.getKeyword()));
            }
        }

        // 필터 조건 추가 (예: inStock 여부)
        if (pageRequestDTO.getInventoryID() != null) {
            builder.and(inventory.inventoryID.eq(pageRequestDTO.getInventoryID()));
        }

        query.where(builder);

        // 데이터 페이징
        JPQLQuery<Tuple> tupleQuery = query.select(
                inventory.inventoryID,
                inventory.sku,
                inventory.quantity,
                inventory.inStock
        );

        List<Tuple> results = getQuerydsl().applyPagination(pageable, tupleQuery).fetch();

        // DTO로 변환
        List<InventoryDTO> dtoList = results.stream().map(tuple -> InventoryDTO.builder()
                .inventoryID(tuple.get(inventory.inventoryID))
                .sku(tuple.get(inventory.sku))
                .quantity(tuple.get(inventory.quantity))
                .inStock(tuple.get(inventory.inStock))
                .build()
        ).collect(Collectors.toList());

        // 전체 데이터 개수
        long total = query.fetchCount();

        return PageResponseDTO.<InventoryDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
