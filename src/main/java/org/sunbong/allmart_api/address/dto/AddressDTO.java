package org.sunbong.allmart_api.address.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String addressID;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private String fullAddress;
}
