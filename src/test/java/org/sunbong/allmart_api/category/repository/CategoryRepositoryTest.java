package org.sunbong.allmart_api.category.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.domain.CategoryProduct;
import org.sunbong.allmart_api.category.dto.CategoryDTO;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.repository.ProductRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Test
    @Rollback(false)
    public void testAddExistingCategoryToProduct() {
        // Given - 반드시 존재하는 카테고리를 사용
        String categoryName = "식료품";
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        Product product = Product.builder().name("포스트맨").sku("post").price(BigDecimal.valueOf(2.99)).build();
        product = productRepository.save(product);

        // CategoryProduct를 사용해 Product에 Category 추가
        CategoryProduct categoryProduct = CategoryProduct.builder()
                .product(product)
                .category(category)
                .build();
        CategoryProduct savedCategoryProduct = categoryProductRepository.save(categoryProduct);

        // Then - 저장된 관계 검증
        assertThat(savedCategoryProduct).isNotNull();
        assertThat(savedCategoryProduct.getCategoryProductID()).isNotNull();
        assertThat(savedCategoryProduct.getProduct().getName()).isEqualTo("포스트맨");
        assertThat(savedCategoryProduct.getCategory().getName()).isEqualTo("식료품");
    }

    @Test
    @Rollback(false)
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
            System.out.println("이미 존재하는 카테고리입니다.");
        }
    }


    @Test
    @Rollback(false)
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
            System.out.println("이미 존재하는 카테고리입니다.");
        }
    }
}