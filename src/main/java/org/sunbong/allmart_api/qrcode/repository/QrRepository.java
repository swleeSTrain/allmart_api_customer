package org.sunbong.allmart_api.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.qrcode.domain.QrCode;


public interface QrRepository extends JpaRepository<QrCode, String> {
}
