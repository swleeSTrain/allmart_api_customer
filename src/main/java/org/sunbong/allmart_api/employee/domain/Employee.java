package org.sunbong.allmart_api.employee.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_employee")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BIGINT")
    private Long employeeID;
    @NotNull
    @Column(length = 30)
    private String name;

    @NotNull
    @Column(columnDefinition = "CHAR(11)")
    private String phoneNumber;

    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "roleid")
    private Role role_id;


}
