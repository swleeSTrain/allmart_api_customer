package org.sunbong.allmart_api.customer.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.dto.CustomerRequestDTO;
import org.sunbong.allmart_api.customer.service.CustomerService;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/signUp/phoneNumber")
    public ResponseEntity<String> phoneNumberSignUp() {
        return ResponseEntity.ok("전화번호설정 페이지?");
    }


    @PostMapping("/signUp/phoneNumber/{phoneNumber}")
    public ResponseEntity<Void> phoneNumberSignUp(@PathVariable("phoneNumber") String phoneNumber ) {
        //
        CustomerRequestDTO customerRequestDTO = CustomerRequestDTO.builder()
                .phoneNumber(phoneNumber)
                .build();

        Optional<Customer> customer = customerService.addMemberWithPhoneNumber(customerRequestDTO);
        Long customerId = customer.get().getCustomerID();

        log.info("전화번호 가등록 회원 생성");
        //return ResponseEntity.ok("전화번호 간편 로그인 성공");
        return ResponseEntity.status(303).header("Location", "/api/v1/qrcode/signUp?phoneNumber=" + phoneNumber + "&customerID=" + customerId)
                .build();
    }
}