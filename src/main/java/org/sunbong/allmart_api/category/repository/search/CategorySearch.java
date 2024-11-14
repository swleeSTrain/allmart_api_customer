package org.sunbong.allmart_api.category.repository.search;

import org.sunbong.allmart_api.category.dto.CategoryListDTO;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;

public interface CategorySearch {

    PageResponseDTO<CategoryListDTO> list(PageRequestDTO pageRequestDTO);
}
