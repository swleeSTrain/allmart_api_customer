package org.sunbong.allmart_api.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.category.domain.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리 이름으로 조회 (중복 체크용)
    Optional<Category> findByName(String name);
}
