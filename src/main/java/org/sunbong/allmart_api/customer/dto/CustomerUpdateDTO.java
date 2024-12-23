package org.sunbong.allmart_api.customer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateDTO {

    Long CustomerID;
    String name;
    String phoneNumber;
    String email;


}
