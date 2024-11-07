package org.sunbong.allmart_api.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.customer.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
