package org.sunbong.allmart_api.category.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.domain.QCategory;
import org.sunbong.allmart_api.category.dto.CategoryDTO;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class CategorySearchImpl extends QuerydslRepositorySupport implements CategorySearch {

    public CategorySearchImpl() {
        super(Category.class);
    }

    @Override
    public PageResponseDTO<CategoryDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("-------------------list----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("categoryID").descending()
        );

        QCategory category = QCategory.category;

        BooleanBuilder builder = new BooleanBuilder();
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        if (keyword != null && type != null) {
            if (type.contains("name")) {
                builder.or(category.name.containsIgnoreCase(keyword));
            }
        }

        JPQLQuery<Category> query = from(category)
                .where(builder);

        // 페이징 적용
        getQuerydsl().applyPagination(pageable, query);

        List<Category> categoryList = query.fetch();
        long total = query.fetchCount();

        // DTO 변환
        List<CategoryDTO> dtoList = categoryList.stream()
                .map(cate -> CategoryDTO.builder()
                        .categoryID(cate.getCategoryID())
                        .name(cate.getName())
                        .build()
                ).collect(Collectors.toList());


        return PageResponseDTO.<CategoryDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    public CategoryDTO readById(Long categoryID) {

        log.info("-------------------read----------");

        QCategory category = QCategory.category;

        JPQLQuery<Category> query = from(category)
                .where(category.categoryID.eq(categoryID));

        Category result = query.fetchOne();

        if(result == null) {
            return null;
        }

        return CategoryDTO.builder()
                .categoryID(result.getCategoryID())
                .name(result.getName())
                .build();
    }
}
