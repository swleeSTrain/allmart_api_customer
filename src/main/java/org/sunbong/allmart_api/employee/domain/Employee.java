package org.sunbong.allmart_api.employee.domain;

import jakarta.persistence.*;

import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_employee")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"roles"})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long employeeID;


    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 11, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    private String password;

    @OneToMany
    @JoinColumn(name = "roleid")
    private Set<Role> roles;


}
