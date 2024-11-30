package org.sunbong.allmart_api.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.sunbong.allmart_api.customer.domain.CustomerMart;

import java.util.Optional;

public interface CustomerMartRepository extends JpaRepository<CustomerMart, Long> {

    @Query("SELECT cm.mart.martID FROM CustomerMart cm WHERE cm.customer.customerID = :customerID")
    Optional<Long> findMartIDByCustomerID(@Param("customerID") Long customerID);
}
