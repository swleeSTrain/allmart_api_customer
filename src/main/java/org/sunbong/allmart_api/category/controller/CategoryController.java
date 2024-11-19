package org.sunbong.allmart_api.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.category.dto.CategoryDTO;
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

    // 조회
    @GetMapping("/{categoryID}")
    public ResponseEntity<CategoryDTO> readById(
            @PathVariable("categoryID") Long categoryID
    ) {
        log.info("=======Category ReadById: {} =======", categoryID);

        return ResponseEntity.ok(categoryService.readById(categoryID));
    }

    // 리스트
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<CategoryDTO>> list(
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
            @RequestBody CategoryDTO dto
    ) throws Exception {

        log.info("=======Category Register=======");

        Long id = categoryService.register(dto);

        return ResponseEntity.ok(id);
    }

    // 삭제
    @DeleteMapping("/{categoryID}")
    public ResponseEntity<Long> delete(@PathVariable Long categoryID) {

        log.info("=======Category Delete: {} =======", categoryID);

        Long deletedID = categoryService.delete(categoryID);

        return ResponseEntity.ok(deletedID);
    }

    @PutMapping("/{categoryID}")
    public ResponseEntity<Long> edit(
            @PathVariable Long categoryID,
            @RequestBody CategoryDTO dto
    ) throws Exception  {

        log.info("=======Category Delete: {} =======", categoryID);

        Long updatedID = categoryService.edit(categoryID, dto);

        return ResponseEntity.ok(updatedID);
    }

}
