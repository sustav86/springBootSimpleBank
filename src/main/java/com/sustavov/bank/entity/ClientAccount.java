package com.sustavov.bank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "client_account")
public class ClientAccount {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount")
    private int amount;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "owner", nullable = false)
    private Client client;

    @JsonIgnoreProperties("clientAccounts")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "client_account_bank_log", joinColumns = @JoinColumn(name = "client_account_id"), inverseJoinColumns = @JoinColumn(name = "bank_log_id"))
    private List<BankLog> bankLogs;

    public void addBankLog(BankLog bankLog) {
        if (bankLogs == null) {
            bankLogs = new ArrayList<>();
        }

        bankLogs.add(bankLog);
    }
}
