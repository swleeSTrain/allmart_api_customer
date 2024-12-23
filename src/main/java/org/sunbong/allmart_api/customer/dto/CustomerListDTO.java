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
    private String name;        // 고객 이름
    private String phoneNumber; // 고객 전화번호
    private String postcode;    // 우편번호
    private String roadAddress; // 도로명 주소
    private String detailAddress; // 상세 주소
    private String fullAddress;  // 전체 주소
    private int loyaltyPoints;

}
