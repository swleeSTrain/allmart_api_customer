package org.sunbong.allmart_api.customer.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sunbong.allmart_api.customer.service.CustomerService;

@Controller
@RestController("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/signUp/phoneNumber")
    public ResponseEntity<String> phoneNumberSignUp() {
        return ResponseEntity.ok("전화번호설정 페이지?");
    }

    @PostMapping("/signUp/phoneNumber/{phoneNum}")
    public ResponseEntity<String> phoneNumberSignUp(@PathVariable("phoneNum") String phoneNumber) {
        return null;
    }


}
