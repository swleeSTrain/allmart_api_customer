package org.sunbong.allmart_api.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {

    public String phoneNumber;
    public String name;
    public int loyaltyPoint;
}
