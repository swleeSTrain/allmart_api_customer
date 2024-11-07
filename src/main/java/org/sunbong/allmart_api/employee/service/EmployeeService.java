package org.sunbong.allmart_api.employee.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.employee.domain.Employee;
import org.sunbong.allmart_api.employee.dto.EmployeeDTO;
import org.sunbong.allmart_api.employee.repository.EmployeeRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeDTO authenticate(String email, String password) {
        Optional<Employee> result = employeeRepository.findByEmail(email);
        return null;
    }

}
