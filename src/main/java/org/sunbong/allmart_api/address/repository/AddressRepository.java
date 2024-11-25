package org.sunbong.allmart_api.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.address.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
