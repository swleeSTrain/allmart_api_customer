package org.sunbong.allmart_api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.product.domain.Product;
import org.sunbong.allmart_api.product.repository.search.ProductSearch;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
}
