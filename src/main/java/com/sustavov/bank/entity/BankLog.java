package com.sustavov.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@Data
@Entity
@Table(name = "bank_log")
public class BankLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated
    private BankOperationType bankOperationType;

    @Column(name = "operation_description")
    private String operationDescription;

    @Column(name = "recipient")
    private int recipient;

    @Column(name = "sender")
    private int sender;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_and_time")
    @CreationTimestamp
    private Date dateTime;

    @JsonIgnoreProperties("bankLogs")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "client_account_bank_log", joinColumns = @JoinColumn(name = "bank_log_id"), inverseJoinColumns = @JoinColumn(name = "client_account_id"))
    private List<ClientAccount> clientAccounts;

    public BankLog() {
    }

    public BankLog(BankOperationType bankOperationType, String operationDescription) {
        this.bankOperationType = bankOperationType;
        this.operationDescription = operationDescription;
    }

    public void addClientAccount(ClientAccount clientAccount) {
        if (clientAccounts == null) {
            clientAccounts = new ArrayList<>();
        }

        clientAccounts.add(clientAccount);
    }
}
