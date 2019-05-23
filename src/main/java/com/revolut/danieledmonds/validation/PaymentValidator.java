package com.revolut.danieledmonds.validation;

import com.revolut.danieledmonds.dao.AccountDao;
import com.revolut.danieledmonds.domain.Account;
import com.revolut.danieledmonds.domain.Payment;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentValidator {

    private final AccountDao accountDao;

    @Inject
    PaymentValidator(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void validatePayment(Payment payment) {

        if(!Integer.toString(payment.getAmount()).matches("[0-9]{1,10}")) {
            throw new IllegalArgumentException("Field 'amount' should be a positive integer of maximum 10 digits");
        }

        if(payment.getDebitingAccountNumber() == null || payment.getDebitingAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Field 'debitingAccountNumber' field should be populated");
        }

        if(payment.getCreditingAccountNumber() == null ||payment.getCreditingAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Field 'creditingAccountNumber' field should be populated");
        }

        Account creditingAccount = accountDao.getAccount(payment.getCreditingAccountNumber());

        if (creditingAccount == null) {
            throw new IllegalArgumentException(String.format("Unable to find 'creditingAccountNumber': %s", payment.getCreditingAccountNumber()));
        }

        Account debitingAccount = accountDao.getAccount(payment.getDebitingAccountNumber());

        if (debitingAccount == null) {
            throw new IllegalArgumentException(String.format("Unable to find 'debitingAccountNumber': %s", payment.getCreditingAccountNumber()));
        }

        if (payment.getAmount() > debitingAccount.getAccountBalance()){
            throw new IllegalArgumentException(String.format("Insufficient funds in debiting account '%s'", payment.getDebitingAccountNumber()));
        }
    }
}
