package org.sunbong.allmart_api.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.sunbong.allmart_api.payment.domain.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p LEFT JOIN FETCH p.order o WHERE o.orderID = :orderID")
    List<Payment> findPaymentsWithOrderById(@Param("orderID") Long orderID);

}
