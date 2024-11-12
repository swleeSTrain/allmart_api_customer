package org.sunbong.allmart_api.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.dto.CustomerResponseDTO;
import org.sunbong.allmart_api.customer.repository.CustomerRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        Optional<Customer> customer = customerRepository.findByPhoneNumber(phoneNumber);
        if (customer.isPresent()) {
            return customer;
        }
        return Optional.empty();
    }

    public Optional<Customer> addMemberWithPhoneNumber(String phoneNumber) {

        CustomerResponseDTO customerDTO = CustomerResponseDTO.builder()
                .phoneNumber(phoneNumber)
                .build();

        return null;

    }



}
