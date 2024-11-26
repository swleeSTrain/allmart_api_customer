package org.sunbong.allmart_api.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequestDTO {
    @NotNull
    private String email;
    @NotNull
    private String pw;
}
