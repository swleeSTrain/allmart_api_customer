package org.sunbong.allmart_api.social;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.allmart_api.customer.dto.CustomerResponseDTO;
import org.sunbong.allmart_api.customer.dto.CustomerTokenResponseDTO;

import org.sunbong.allmart_api.customer.service.CustomerService;
import org.sunbong.allmart_api.security.util.JWTUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/kakao")
public class KakaoController {

    final private JWTUtil jwtUtil;
    final private CustomerService customerService;


    @GetMapping("/oauth/{email}")
    public ResponseEntity<CustomerTokenResponseDTO> makeSocialSinInData(@PathVariable String email){

        CustomerResponseDTO customer = customerService.findByEmail(email);

        Map<String, Object> data = new HashMap<>();
        String accessToken = jwtUtil.createToken(data, 60);
        String refreshToken = jwtUtil.createToken(data, 1440);

        return ResponseEntity.ok(CustomerTokenResponseDTO.builder()
                        .phoneNumber(customer.getPhoneNumber())
                        .name(customer.name)
                        .loyaltyPoints(customer.loyaltyPoint)
                        .accessToken(accessToken)
                        .email(email)
                        .refreshToken(refreshToken)
                        .build());


    }
}
