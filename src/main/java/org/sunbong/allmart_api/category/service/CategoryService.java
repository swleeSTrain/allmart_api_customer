package org.sunbong.allmart_api.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.dto.CategoryAddDTO;
import org.sunbong.allmart_api.category.dto.CategoryListDTO;
import org.sunbong.allmart_api.category.repository.CategoryRepository;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 리스트
    public PageResponseDTO<CategoryListDTO> list(PageRequestDTO pageRequestDTO) {

        PageResponseDTO<CategoryListDTO> result = categoryRepository.list(pageRequestDTO);

        return result;
    }

    // 등록
    public Long register(CategoryAddDTO dto) throws Exception {
        // 중복 체크
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new Exception("이미 존재하는 카테고리입니다: " + dto.getName());
        }

        // DTO를 엔티티로 변환 후 저장
        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getCategoryID();
    }
}
