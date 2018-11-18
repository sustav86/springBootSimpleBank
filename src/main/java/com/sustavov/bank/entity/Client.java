package com.sustavov.bank.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@Data
@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "phone", length = 13)
    private String phone;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ClientAccount> clientAccounts;


    public void addClientAccount(ClientAccount clientAccount) {
        if (clientAccounts == null) {
            clientAccounts = new ArrayList<>();
        }

        clientAccounts.add(clientAccount);
        clientAccount.setClient(this);
    }
}
