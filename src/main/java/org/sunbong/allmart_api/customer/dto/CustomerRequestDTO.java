package org.sunbong.allmart_api.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {

    @NotNull
    private String phoneNumber;
    private LocalDateTime createdDate;


}
