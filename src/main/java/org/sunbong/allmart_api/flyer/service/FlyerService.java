package org.sunbong.allmart_api.flyer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.flyer.dto.FlyerListDTO;
import org.sunbong.allmart_api.flyer.dto.FlyerReadDTO;
import org.sunbong.allmart_api.flyer.repository.FlyerRepository;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FlyerService {

    private final FlyerRepository flyerRepository;

    // 조회
    public FlyerReadDTO readById(Long id) {

        FlyerReadDTO result = flyerRepository.readById(id);

        return result;
    }

    // 리스트
    public PageResponseDTO<FlyerListDTO> list(PageRequestDTO pageRequestDTO) {

        PageResponseDTO<FlyerListDTO> result = flyerRepository.list(pageRequestDTO);

        return result;
    }



}
