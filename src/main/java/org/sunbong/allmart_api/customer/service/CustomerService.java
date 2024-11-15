package org.sunbong.allmart_api.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.dto.CustomerRequestDTO;
import org.sunbong.allmart_api.customer.repository.CustomerRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 전화번호 조회
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {

        Optional<Customer> customerOne = customerRepository.findByPhoneNumber(phoneNumber);
        if (customerOne.isPresent()) {
            return customerOne;
        }
        return Optional.empty();
    }

    // 간편 전화번호 고객 생성
    public Optional<Customer> addMemberWithPhoneNumber(CustomerRequestDTO customerRequestDTO) {

        Customer customer = Customer.builder()
                .phoneNumber(customerRequestDTO.getPhoneNumber())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return Optional.of(savedCustomer);
    }

    // 아이디로 삭제 (관리자용)
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    // 폰번호로 삭제 (관리자용)
    public void deleteCustomerByPhoneNumber(String phoneNumber) {
        customerRepository.deleteByPhoneNumber(phoneNumber);
    }







}
