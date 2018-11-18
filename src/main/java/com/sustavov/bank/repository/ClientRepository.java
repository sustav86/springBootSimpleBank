package com.sustavov.bank.repository;

import com.sustavov.bank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client findByName(String name);
}
