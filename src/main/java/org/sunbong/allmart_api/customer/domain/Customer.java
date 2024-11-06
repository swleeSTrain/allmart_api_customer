package org.sunbong.allmart_api.customer.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.sunbong.allmart_api.common.domain.BaseEntity;

@Entity
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString(callSuper = true)
@Table(name = "tbl_customer")
public class Customer extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;

    @NotNull
    @Column(length = 11)
    private int phoneNumber;

    @NotNull
    @Column(length = 30)
    private String name;

    @Builder.Default
    private int loyaltyPoints = 0;

}
