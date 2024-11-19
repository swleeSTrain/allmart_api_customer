package org.sunbong.allmart_api.qrcode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrSingUpResponseDTO {
    private String filePath;
    private String qrCodeUrl;
}
