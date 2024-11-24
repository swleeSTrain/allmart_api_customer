package org.sunbong.allmart_api.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.dto.CategoryDTO;
import org.sunbong.allmart_api.category.repository.CategoryRepository;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 조회
    public CategoryDTO readById(Long id) {

        CategoryDTO result = categoryRepository.readById(id);

        return result;
    }

    // 리스트
    public PageResponseDTO<CategoryDTO> list(PageRequestDTO pageRequestDTO) {

        PageResponseDTO<CategoryDTO> result = categoryRepository.list(pageRequestDTO);

        return result;
    }

    // 등록
    public Long register(CategoryDTO dto) throws Exception {

        // 중복 체크
        validateDuplicate(dto.getName());

        // DTO를 엔티티로 변환 후 저장
        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return savedCategory.getCategoryID();
    }

    // 삭제
    public Long delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with ID: " + id));

        Long categoryID = category.getCategoryID();
        categoryRepository.deleteById(categoryID);

        return categoryID;
    }

    // 수정
    public Long edit(Long id, CategoryDTO dto) throws Exception {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        // 카테고리 이름이 변경된 경우, 자기 이름은 빼고 중복 검사
        if (!category.getName().equals(dto.getName())) {
            validateDuplicate(dto.getName());
        }

        category = category.toBuilder()
                .name(dto.getName())
                .build();

        Category updatedCategory = categoryRepository.save(category);

        return updatedCategory.getCategoryID();
    }

    // 중복 처리
    private void validateDuplicate(String name) throws Exception {
        if (categoryRepository.findByName(name).isPresent()) {

            // 409 상태 코드 반환
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다: " + name);
        }
    }

}
