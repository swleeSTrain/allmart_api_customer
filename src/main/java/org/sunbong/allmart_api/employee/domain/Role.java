package org.sunbong.allmart_api.employee.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_role")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role { //enum
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleID;

    private String roleName;

    private String permissions;
}
