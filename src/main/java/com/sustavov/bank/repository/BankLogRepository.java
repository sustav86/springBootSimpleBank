package com.sustavov.bank.repository;

import com.sustavov.bank.entity.BankLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
public interface BankLogRepository extends JpaRepository<BankLog, Integer> {

    @Query(value = "select * " +
            "from bank_log as bl " +
            "inner join client_account_bank_log as ca_bl " +
            "on bl.id=ca_bl.bank_log_id " +
            "where ca_bl.client_account_id=(:cardId)",
            nativeQuery = true)
    List<BankLog> findBankLogByCard(@Param("cardId") int cardId);

    @Query(value = "select * " +
            "from bank_log as bl " +
            "inner join client_account_bank_log as ca_bl " +
            "on bl.id=ca_bl.bank_log_id " +
            "where ca_bl.client_account_id=(:cardId) and bl.date_and_time between (:fromDate) and (:toDate)",
            nativeQuery = true)
    List<BankLog> findBankLogByCardByDate(@Param("cardId") int cardId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "select * " +
            "from bank_log as bl " +
            "where bl.id in " +
            "(select ca_bl.bank_log_id " +
            "from client_account_bank_log as ca_bl " +
            "where ca_bl.client_account_id in " +
            "(select ca.id " +
            "from client_account as ca " +
            "where ca.owner=(:clientId)))",
            nativeQuery = true)
    List<BankLog> findBankLogByClient(@Param("clientId") int clientId);

    @Query(value = "select * " +
            "from bank_log as bl " +
            "where bl.date_and_time between (:fromDate) and (:toDate) and bl.id in " +
            "(select ca_bl.bank_log_id " +
            "from client_account_bank_log as ca_bl " +
            "where ca_bl.client_account_id in " +
            "(select ca.id " +
            "from client_account as ca " +
            "where ca.owner=(:clientId)))",
            nativeQuery = true)
    List<BankLog> findBankLogByClientByDate(@Param("clientId") int clientId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
