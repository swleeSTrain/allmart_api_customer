package org.sunbong.allmart_api.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.inventory.domain.Inventory;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
