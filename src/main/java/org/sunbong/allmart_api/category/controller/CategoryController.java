package org.sunbong.allmart_api.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.allmart_api.category.dto.CategoryAddDTO;
import org.sunbong.allmart_api.category.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
@Log4j2
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("add")
    public ResponseEntity<Long> register(
            @ModelAttribute CategoryAddDTO dto
    ) throws Exception {

        log.info("=======Category Register=======");

        Long id = categoryService.register(dto);

        return ResponseEntity.ok(id);
    }

}
