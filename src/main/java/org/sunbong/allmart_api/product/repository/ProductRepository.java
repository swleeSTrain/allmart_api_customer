package org.sunbong.allmart_api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.repository.search.ProductSearch;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
    Optional<Product> findByName(String name); // 상품명으로 Product 조회
}
