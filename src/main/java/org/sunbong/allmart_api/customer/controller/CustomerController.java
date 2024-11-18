package org.sunbong.allmart_api.customer.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.customer.dto.CustomerRequestDTO;
import org.sunbong.allmart_api.customer.service.CustomerService;


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

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/signUp/phoneNumber/{phoneNumber}")
    public ResponseEntity<String> phoneNumberSignUp(@PathVariable("phoneNumber") String phoneNumber ) {

        CustomerRequestDTO customerRequestDTO = CustomerRequestDTO.builder()
                .phoneNumber(phoneNumber)
                .build();

        customerService.addMemberWithPhoneNumber(customerRequestDTO);
        log.info("전화번호 가등록 회원 생성");
        return ResponseEntity.ok("전화번호 간편 로그인 성공");
    }
}