package org.sunbong.allmart_api.customer.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.dto.CustomerRequestDTO;
import org.sunbong.allmart_api.customer.dto.CustomerUpdateDTO;
import org.sunbong.allmart_api.customer.repository.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 조회
    // 1.전화번호 조회
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {

        Optional<Customer> customerOne = customerRepository.findByPhoneNumber(phoneNumber);
        if (customerOne.isPresent()) {
            return customerOne;
        }
        return Optional.empty();
    }
    // 2. 이름 조회
    public Optional<List<Customer>> findByNames(String name) {
        Optional<List<Customer>> customers = customerRepository.findByName(name);
        if (customers.isPresent()) {
            customers.get().forEach(customer -> {
                log.info("Customer: " + customer.getName());
            });
            return customers;
        }
        return Optional.empty();
    }

    //3. 리스트 조회
    public Page<Customer> list (Pageable pageable) {
        Page<Customer> list = customerRepository.list(pageable);
        return list;
    }

    // 생성

    // 1.간편 전화번호 고객 생성
    public Optional<Customer> addMemberWithPhoneNumber(CustomerRequestDTO customerRequestDTO) {

        Customer customer = Customer.builder()
                .phoneNumber(customerRequestDTO.getPhoneNumber())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return Optional.of(savedCustomer);
    }

    //----------------------------------------------------------------

    //삭제

    // 1.아이디로 삭제 (관리자용)
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    // 2.폰번호로 삭제 (관리자용)
    public void deleteCustomerByPhoneNumber(String phoneNumber) {
        customerRepository.deleteByPhoneNumber(phoneNumber);
    }

    // 3.회원 삭제
    public void deleteAllCustomers() {
        customerRepository.deleteAll();
    }

    //----------------------------------------------------------------

    //수정
    public Optional<Customer> update(CustomerUpdateDTO updateDTO) {
        //조회
        Optional<Customer> customerOptional = findByPhoneNumber(updateDTO.getPhoneNumber());
        Customer customer = customerOptional.orElseThrow(() -> new NoSuchElementException("Customer not found"));
            Customer updatedCustomer = customer.toBuilder()
                    .phoneNumber(updateDTO.getPhoneNumber())
                    .name(updateDTO.getName())
                    .build();
            customerRepository.save(updatedCustomer);
            return Optional.of(updatedCustomer);
        }


    // 중복가입 여부 확인
    public void checkDuplicateRegistration(CustomerRequestDTO customerRequestDTO) {
        if (customerRepository.existsByPhoneNumber(customerRequestDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }


    }

    public String createCookie(Customer customer, HttpServletResponse response) {
        Cookie cookie = new Cookie("customer", customer.getPhoneNumber());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Https환경에서만 전송( 개발 중에는 false 가능)
        cookie.setPath("/"); //모든 경로에서 유효
        cookie.setMaxAge(60 * 60); // 1시간

        response.addCookie(cookie);

        //응답에 쿠키 추가
        return "쿠키가 설정되었습니다";

    }


}
