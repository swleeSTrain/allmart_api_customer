package org.sunbong.allmart_api.flyer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.common.exception.CommonExceptions;
import org.sunbong.allmart_api.flyer.dto.FlyerListDTO;
import org.sunbong.allmart_api.flyer.dto.FlyerReadDTO;
import org.sunbong.allmart_api.flyer.service.FlyerService;

@RestController
@RequestMapping("/api/v1/flyer")
@Log4j2
@RequiredArgsConstructor
public class FlyerController {

    private final FlyerService flyerService;

    // 조회
    @GetMapping("/{flyerID}")
    public ResponseEntity<FlyerReadDTO> readById(
            @PathVariable("flyerID") Long flyerID
    ) {
        log.info("=======Flyer ReadById: {} =======", flyerID);

        return ResponseEntity.ok(flyerService.readById(flyerID));
    }

    // 목록
    @GetMapping("list")
    public ResponseEntity<PageResponseDTO<FlyerListDTO>> list(
            @Validated PageRequestDTO pageRequestDTO
    ) {
        log.info("=======Flyer List=======");

        // 페이지 번호가 0보다 작으면 예외 발생
        if (pageRequestDTO.getPage() < 0) {
            throw CommonExceptions.LIST_ERROR.get();
        }

        return ResponseEntity.ok(flyerService.list(pageRequestDTO));
    }
}
