package org.sunbong.allmart_api.category.repository.search;

import org.sunbong.allmart_api.category.dto.CategoryDTO;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;

public interface CategorySearch {

    PageResponseDTO<CategoryDTO> list(PageRequestDTO pageRequestDTO);

    CategoryDTO readById(Long categoryID);
}
