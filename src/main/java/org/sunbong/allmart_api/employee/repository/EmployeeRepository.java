package org.sunbong.allmart_api.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.sunbong.allmart_api.employee.domain.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

}
