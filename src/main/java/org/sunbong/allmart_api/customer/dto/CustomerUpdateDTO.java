package org.sunbong.allmart_api.customer.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateDTO {

    String name;

    @NotNull(message = "Phone number cannot be null")
    String phoneNumber;


}
