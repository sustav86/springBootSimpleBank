package com.sustavov.bank.restcontroller;

import com.sustavov.bank.entity.BankLog;
import com.sustavov.bank.entity.Client;
import com.sustavov.bank.error.CustomNotFountException;
import com.sustavov.bank.repository.BankLogRepository;
import com.sustavov.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@RestController
@RequestMapping("/log")
public class BankLogController {

    @Autowired
    private BankLogRepository bankLogRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/card/{cardId}")
    public List<BankLog> findByCard(@PathVariable int cardId) {

        return bankLogRepository.findBankLogByCard(cardId);
    }

    @GetMapping("/datecard/{cardId}")
    public List<BankLog> findByCardByDate(@PathVariable int cardId,
                                          @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                          @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        return bankLogRepository.findBankLogByCardByDate(cardId, fromDate, toDate);
    }

    @GetMapping("/client/{name}")
    public List<BankLog> findByClient(@PathVariable String name) {

        Client byName = clientRepository.findByName(name);

        if (byName == null) {
            throw new CustomNotFountException("Client not found: " + name);
        }

        return bankLogRepository.findBankLogByClient(byName.getId());
    }

    @GetMapping("/dateclient/{name}")
    public List<BankLog> findByClientByDate(@PathVariable String name,
                                            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        Client byName = clientRepository.findByName(name);

        if (byName == null) {
            throw new CustomNotFountException("Client not found: " + name);
        }

        return bankLogRepository.findBankLogByClientByDate(byName.getId(), fromDate, toDate);
    }
}
