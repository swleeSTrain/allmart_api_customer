package org.sunbong.allmart_api.flyer.repository.search;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.flyer.domain.Flyer;
import org.sunbong.allmart_api.flyer.dto.FlyerListDTO;
import org.sunbong.allmart_api.flyer.dto.FlyerReadDTO;

public class FlyerSearchImpl extends QuerydslRepositorySupport implements FlyerSearch {

    public FlyerSearchImpl() {
        super(Flyer.class);
    }

    @Override
    public PageResponseDTO<FlyerListDTO> list(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("productID").descending()
        );


        return null;
    }

    @Override
    public FlyerReadDTO readById(Long productID) {
        return null;
    }
}
