package org.sunbong.allmart_api.category.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.dto.CategoryAddDTO;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @Rollback(false)
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
                System.out.println(categoryName + "는 이미 존재하는 카테고리입니다.");
            }
        }

        // 검증
        assertThat(categoryRepository.findAll().size()).isGreaterThanOrEqualTo(100);
    }

    @Test
    @Rollback(false)
    public void testAddCategory1() {
        // Given
        CategoryAddDTO categoryAddDTO = new CategoryAddDTO();
        categoryAddDTO.setName("식료품");

        // 카테고리 이름으로 중복 체크
        if (!categoryRepository.findByName(categoryAddDTO.getName()).isPresent()) {
            // CategoryDTO를 Category 엔티티로 변환
            Category category = Category.builder()
                    .name(categoryAddDTO.getName())
                    .build();

            // When
            Category savedCategory = categoryRepository.save(category);

            // Then
            assertThat(savedCategory).isNotNull();
            assertThat(savedCategory.getCategoryID()).isNotNull();
            assertThat(savedCategory.getName()).isEqualTo("식료품");
        } else {
            System.out.println("이미 존재하는 카테고리입니다.");
        }
    }


    @Test
    @Rollback(false)
    public void testAddCategory() {
        // Given
        CategoryAddDTO categoryAddDTO = new CategoryAddDTO();
        categoryAddDTO.setName("포켓몬");

        // 카테고리 이름으로 중복 체크
        if (!categoryRepository.findByName(categoryAddDTO.getName()).isPresent()) {
            // CategoryDTO를 Category 엔티티로 변환
            Category category = Category.builder()
                    .name(categoryAddDTO.getName())
                    .build();

            // When
            Category savedCategory = categoryRepository.save(category);

            // Then
            assertThat(savedCategory).isNotNull();
            assertThat(savedCategory.getCategoryID()).isNotNull();
            assertThat(savedCategory.getName()).isEqualTo("포켓몬");
        } else {
            System.out.println("이미 존재하는 카테고리입니다.");
        }
    }
}