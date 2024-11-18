package org.sunbong.allmart_api.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.inventory.domain.Inventory;
import org.sunbong.allmart_api.inventory.repository.search.InventorySearch;

public interface InventoryRepository extends JpaRepository<Inventory, Long>, InventorySearch {

}
