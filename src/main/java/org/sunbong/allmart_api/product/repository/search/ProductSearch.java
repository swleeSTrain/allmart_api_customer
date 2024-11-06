package org.sunbong.allmart_api.product.repository.search;

import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.product.dto.ProductListDTO;

public interface ProductSearch {

    PageResponseDTO<ProductListDTO> productList(PageRequestDTO pageRequestDTO);
}
