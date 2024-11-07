package org.sunbong.allmart_api.employee.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequestDTO {
    @NotNull
    private String email;
    @NotNull
    private String pw;
}
