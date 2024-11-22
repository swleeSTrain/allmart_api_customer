package org.sunbong.allmart_api.inventory.repository.search;

import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.inventory.dto.InventoryListDTO;

public interface InventorySearch {
    PageResponseDTO<InventoryListDTO> searchInventory(PageRequestDTO pageRequestDTO);
}
