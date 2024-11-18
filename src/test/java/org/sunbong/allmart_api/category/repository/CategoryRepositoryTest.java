package org.sunbong.allmart_api.category.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.dto.CategoryDTO;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@Commit
@Log4j2
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;



    @Test
    public void testDelete() {

        Long CategoryID = 109L;

        categoryRepository.deleteById(CategoryID);

        boolean exists = categoryRepository.findById(CategoryID).isPresent();

        assertTrue(!exists, "Product should be deleted");
    }


    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        PageResponseDTO<CategoryDTO> result = categoryRepository.list(pageRequestDTO);

        // DTO 리스트 출력
        result.getDtoList().forEach(dto -> {
            log.info("Category DTO: " + dto);
        });

        // 검증
        assertNotNull(result, "Result should not be null");
    }


    @Test
    public void testAddMCategoryDummies() {
        // 100개의 더미 카테고리를 생성하여 저장하는 반복문
        for (int i = 1; i <= 100; i++) {
            String categoryName = "카테고리_" + i;

            // 카테고리 이름으로 중복 체크
            if (!categoryRepository.findByName(categoryName).isPresent()) {
                // Category 엔티티 생성 및 저장
                Category category = Category.builder()
                        .name(categoryName)
                        .build();

                categoryRepository.save(category);
            } else {
                log.info(categoryName + "는 이미 존재하는 카테고리입니다.");
            }
        }

        // 검증
        assertThat(categoryRepository.findAll().size()).isGreaterThanOrEqualTo(100);
    }

    @Test
    public void testAddCategory1() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("식료품");

        // 카테고리 이름으로 중복 체크
        if (!categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            // CategoryDTO를 Category 엔티티로 변환
            Category category = Category.builder()
                    .name(categoryDTO.getName())
                    .build();

            // When
            Category savedCategory = categoryRepository.save(category);

            // Then
            assertThat(savedCategory).isNotNull();
            assertThat(savedCategory.getCategoryID()).isNotNull();
            assertThat(savedCategory.getName()).isEqualTo("식료품");
        } else {
            log.info("이미 존재하는 카테고리입니다.");
        }
    }


    @Test
    public void testAddCategory() {
        // Given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("포켓몬");

        // 카테고리 이름으로 중복 체크
        if (!categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            // CategoryDTO를 Category 엔티티로 변환
            Category category = Category.builder()
                    .name(categoryDTO.getName())
                    .build();

            // When
            Category savedCategory = categoryRepository.save(category);

            // Then
            assertThat(savedCategory).isNotNull();
            assertThat(savedCategory.getCategoryID()).isNotNull();
            assertThat(savedCategory.getName()).isEqualTo("포켓몬");
        } else {
            log.info("이미 존재하는 카테고리입니다.");
        }
    }
}