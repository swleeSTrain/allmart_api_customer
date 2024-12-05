package org.sunbong.allmart_api.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sunbong.allmart_api.customer.domain.CustomerLoginType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDTO {

    private String phoneNumber;

    private String email;

    private CustomerLoginType loginType;

    private LocalDateTime createdDate;


}
