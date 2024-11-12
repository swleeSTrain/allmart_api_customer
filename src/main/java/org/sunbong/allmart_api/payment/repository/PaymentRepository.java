package org.sunbong.allmart_api.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
