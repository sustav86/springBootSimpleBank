package com.sustavov.bank.restcontroller;

import com.sustavov.bank.entity.ClientAccount;
import com.sustavov.bank.error.CustomNotFountException;
import com.sustavov.bank.repository.BankLogRepository;
import com.sustavov.bank.repository.ClientAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@RestController
@RequestMapping("/account")
public class ClientAccountController {

    private final static String SENDER = "sender";
    private final static String RECIPIENT = "recipient";
    private final static String AMOUNT = "amount";

    @Autowired
    private ClientAccountRepository clientAccountRepository;
    @Autowired
    private BankLogRepository bankLogRepository;

    @PostMapping("/transfer")
    public Map<String, ClientAccount> transfer(@RequestBody Map<String, Integer> payload) {

        int senderId = payload.get(SENDER);
        int recipientId = payload.get(RECIPIENT);
        int amount = payload.get(AMOUNT);

        ClientAccount sender = clientAccountRepository.findById(senderId).orElseThrow(() -> new CustomNotFountException("Not fount sender: " + senderId));
        ClientAccount recipient = clientAccountRepository.findById(recipientId).orElseThrow(() -> new CustomNotFountException("Not fount recipient: " + recipientId));
        int senderAmount = sender.getAmount();

        if (amount < 0 || senderAmount < amount) {
            throw new CustomNotFountException("The sender:" + senderId + " does not have enough money for the transaction");
        }

        sender.setAmount(senderAmount - amount);
        recipient.setAmount(recipient.getAmount() + amount);

        Map<String, ClientAccount> accountMap = new HashMap<>();
        accountMap.put(SENDER, sender);
        accountMap.put(RECIPIENT, recipient);

        clientAccountRepository.saveAll(accountMap.values());

        return accountMap;
    }
}
