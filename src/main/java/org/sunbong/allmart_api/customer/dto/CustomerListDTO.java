package org.sunbong.allmart_api.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListDTO {

    private Long customerID;
    private String name;
    private int price;
    private String phoneNumber;
    private int loyaltyPoints;

}
