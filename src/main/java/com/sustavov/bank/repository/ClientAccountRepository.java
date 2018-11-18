package com.sustavov.bank.repository;

import com.sustavov.bank.entity.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Integer> {
}
