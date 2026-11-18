package org.sample.customeraccountmanagement.account.repository;

import org.sample.customeraccountmanagement.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCustomer_Id(Long customerId);

    @Query(value = "SELECT NEXT VALUE FOR account_number_seq", nativeQuery = true)
    Long nextAccountNumber();
}
