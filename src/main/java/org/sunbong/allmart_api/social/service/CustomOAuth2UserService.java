package org.sunbong.allmart_api.social.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.domain.CustomerLoginType;
import org.sunbong.allmart_api.customer.repository.CustomerRepository;
import org.sunbong.allmart_api.customer.service.CustomerService;
import org.sunbong.allmart_api.social.dto.SocialCustomerDTO;
import org.sunbong.allmart_api.security.util.JWTUtil;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final CustomerRepository customerRepository;

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("-------------------");
        log.info("-------------------");
        log.info(userRequest);

        String serviceName = userRequest.getClientRegistration().getClientName();

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String principalName = oAuth2User.getAttribute("email");
        if (principalName == null || principalName.isEmpty()) {
            principalName = "defaultPrincipal"; // 기본값 설정
        }

        java.util.Map<String, Object>  paramMap = oAuth2User.getAttributes();

        paramMap.forEach((k,v) -> {
            log.info("key: " + k + " value: " + v);
        });

        log.info("-------------------");
        log.info("-------------------");
        log.info("-------------------");

        log.info("===========================");
        log.info(serviceName);

        LinkedHashMap accountMap = (LinkedHashMap) paramMap.get("kakao_account");

        String email = (String) accountMap.get("email");
        String name = (String) accountMap.get("name");

        log.info("email: " + email);

        log.info("-----------------------------------------------------");
        log.info("-----------------------------------------------------");
        log.info("-----------------------------------------------------");

        if(customerRepository.findByEmail(email) == null){
            customerRepository.save(Customer.builder()
                    .email(email)
                    .loginType(CustomerLoginType.SOCIAL)
                    .build());

        }

        // JWT 토큰 생성
        Map<String, Object> claims = new HashMap<>();
        String accessToken = jwtUtil.createToken(claims, 60);
        String refreshToken = jwtUtil.createToken(claims, 1440);

        SocialCustomerDTO customerDTO = new SocialCustomerDTO();
        customerDTO.setName(principalName);
        customerDTO.setRoles(java.util.List.of("USER"));
        customerDTO.setEmail(email);
        customerDTO.setAccessToken(accessToken);
        customerDTO.setRefreshToken(refreshToken);

        return customerDTO;
    }



}
