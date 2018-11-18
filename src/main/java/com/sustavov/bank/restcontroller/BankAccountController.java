package com.sustavov.bank.restcontroller;

import com.sustavov.bank.entity.BankAccount;
import com.sustavov.bank.entity.ClientAccount;
import com.sustavov.bank.error.CustomNotFountException;
import com.sustavov.bank.repository.BankAccountRepository;
import com.sustavov.bank.repository.ClientAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@RestController
@RequestMapping("/bank")
public class BankAccountController {

    public final static int BANK_ID = 1;
    private final static String ACCOUNT = "account";
    private final static String AMOUNT = "amount";
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private ClientAccountRepository clientAccountRepository;

    @PostMapping("/buying")
    public ClientAccount buying(@RequestBody Map<String, Integer> payload) {
        int amount = payload.get(AMOUNT);
        int accountId = payload.get(ACCOUNT);

        BankAccount bankAccount = bankAccountRepository.findById(BANK_ID).orElseThrow(IllegalArgumentException::new);
        int bankAmount = bankAccount.getAmount();
        if (amount < 0 || bankAmount < amount) {
            throw new CustomNotFountException("Not enough money for a transaction");
        }
        bankAccount.setAmount(bankAmount - amount);

        ClientAccount clientAccount = clientAccountRepository.findById(accountId).orElseThrow(IllegalArgumentException::new);
        clientAccount.setAmount(clientAccount.getAmount() + amount);

        bankAccountRepository.save(bankAccount);
        clientAccountRepository.save(clientAccount);

        return clientAccount;
    }

    @PutMapping("/emission")
    public BankAccount emission(@RequestBody BankAccount bankAccount) {
        bankAccount.setId(BANK_ID);
        Optional<BankAccount> byId = bankAccountRepository.findById(BANK_ID);
        if (byId.isPresent()) {
            BankAccount currentBankAccount = byId.get();
            bankAccount.setAmount(currentBankAccount.getAmount() + bankAccount.getAmount());
        }

        bankAccountRepository.save(bankAccount);

        return bankAccount;
    }
}
