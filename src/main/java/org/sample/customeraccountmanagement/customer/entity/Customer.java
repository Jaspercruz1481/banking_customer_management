package org.sample.customeraccountmanagement.customer.entity;

import lombok.*;
import org.sample.customeraccountmanagement.account.entity.Account;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 50)
    private String customerName;

    @Column(name = "customer_mobile", nullable = false, unique = true, length = 20)
    private String customerMobile;

    @Column(name = "customer_email", nullable = false, length = 50)
    private String customerEmail;

    @Column(name = "address1", nullable = false, length = 100)
    private String address1;

    @Column(name = "address2", length = 100)
    private String address2;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Account> accounts;
}
