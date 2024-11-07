package org.sunbong.allmart_api.employee.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String email;
    private String accessToken;
    private String refreshToken;
}
