package org.sunbong.allmart_api.qrcode.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class QrRequestDto {

    @NotNull
    private String phoneNumber;

    private Long customerID;

    private Long martID;

}
