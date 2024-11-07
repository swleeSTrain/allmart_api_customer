package org.sunbong.allmart_api.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.category.domain.Category;
import org.sunbong.allmart_api.category.domain.CategoryProduct;
import org.sunbong.allmart_api.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {

    public List<CategoryProduct> findByProductProductID(Long productID);

    // Product와 Category로 CategoryProduct 조회, 중복 방지
    Optional<CategoryProduct> findByProductAndCategory(Product product, Category category);
}