package org.sunbong.allmart_api.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.category.dto.CategoryAddDTO;
import org.sunbong.allmart_api.category.dto.CategoryListDTO;
import org.sunbong.allmart_api.category.service.CategoryService;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.common.exception.CommonExceptions;

@RestController
@RequestMapping("/api/v1/category")
@Log4j2
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 리스트
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<CategoryListDTO>> list(
            @Validated PageRequestDTO pageRequestDTO
    ) {
        log.info("=======Category List=======");

        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        return ResponseEntity.ok(categoryService.list(pageRequestDTO));
    }

    // 등록
    @PostMapping("add")
    public ResponseEntity<Long> register(
            @RequestBody CategoryAddDTO dto
    ) throws Exception {

        log.info("=======Category Register=======");

        Long id = categoryService.register(dto);

        return ResponseEntity.ok(id);
    }

}
