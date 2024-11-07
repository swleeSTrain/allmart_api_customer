package org.sunbong.allmart_api.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.category.domain.CategoryProduct;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
}