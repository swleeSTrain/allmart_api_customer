package org.sunbong.allmart_api.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.customer.domain.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> deleteByPhoneNumber(String phoneNumber);
}
