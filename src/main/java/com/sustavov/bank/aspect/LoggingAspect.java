package com.sustavov.bank.aspect;

import com.sustavov.bank.entity.BankAccount;
import com.sustavov.bank.entity.BankLog;
import com.sustavov.bank.entity.BankOperationType;
import com.sustavov.bank.entity.ClientAccount;
import com.sustavov.bank.repository.BankAccountRepository;
import com.sustavov.bank.repository.BankLogRepository;
import com.sustavov.bank.restcontroller.BankAccountController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author Anton Sustavov
 * @since 2018/11/15
 */
@Aspect
@Configuration
public class LoggingAspect {

    private static final String CLIENT = "client";
    private static final String SENDER = "sender";
    private static final String RECIPIENT = "recipient";

    @Autowired
    private BankLogRepository bankLogRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterReturning(value = "execution(* com.sustavov.bank.restcontroller.BankAccountController.*(..))",
            returning = "result")
    public void afterReturningBankAccountController(JoinPoint joinPoint, Object result) {

        BankLog bankLog;
        String method = joinPoint.getSignature().getName().toUpperCase();
        BankOperationType bankOperationType = BankOperationType.valueOf(method);

        if (bankOperationType == BankOperationType.EMISSION) {
            bankLog = new BankLog(BankOperationType.EMISSION, BankOperationType.EMISSION.toString());
        } else {
            BankAccount bankAccount = bankAccountRepository.findById(BankAccountController.BANK_ID).orElseThrow(IllegalArgumentException::new);
            bankLog = new BankLog(BankOperationType.BUYING, BankOperationType.BUYING.toString());
            bankLog.setSender(bankAccount.getId());
            bankLog.setRecipient(((ClientAccount) result).getId());
        }

        bankLogRepository.save(bankLog);
    }

    @AfterReturning(value = "execution(* com.sustavov.bank.restcontroller.BankLogController.*(..))",
            returning = "result")
    public void afterReturningBankLogController(JoinPoint joinPoint, List<BankLog> result) {

        String method = joinPoint.getSignature().getName().toLowerCase();
        BankLog bankLog;
        if (method.contains(CLIENT)) {
            bankLog = new BankLog(BankOperationType.HISTORY_CLIENT, BankOperationType.HISTORY_CLIENT.toString());
        } else {
            bankLog = new BankLog(BankOperationType.HISTORY_CARD, BankOperationType.HISTORY_CARD.toString());
        }

        bankLogRepository.save(bankLog);
    }

    @AfterReturning(value = "execution(* com.sustavov.bank.restcontroller.ClientAccountController.*(..))",
            returning = "result")
    public void afterReturningClientAccountController(JoinPoint joinPoint, Map<String, ClientAccount> result) {

        BankLog bankLog = new BankLog(BankOperationType.TRANSFER, BankOperationType.TRANSFER.toString());
        ClientAccount sender = result.get(SENDER);
        ClientAccount recipient = result.get(RECIPIENT);
        bankLog.setSender(sender.getId());
        bankLog.setRecipient(recipient.getId());
        bankLog.addClientAccount(sender);
        bankLog.addClientAccount(recipient);

        bankLogRepository.save(bankLog);
    }
}
