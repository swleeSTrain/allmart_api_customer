package org.sunbong.allmart_api.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.exception.CommonExceptions;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.dto.CustomerRequestDTO;
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

    public Optional<Customer> addCustomerWithPhoneNumber(CustomerRequestDTO requestDTO) {

        Customer customer = Customer.builder()
                .phoneNumber(requestDTO.getPhoneNumber())
                .build();

        return Optional.of(customer);
    }

    public Optional<Customer> list(PageRequestDTO PageRequestDTO) {

        if(PageRequestDTO.getPage() < 0){
            throw CommonExceptions.LIST_ERROR.get();
        }
        return null;
    }



}
