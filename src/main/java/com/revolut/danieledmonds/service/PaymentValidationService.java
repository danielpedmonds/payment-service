package com.revolut.danieledmonds.service;

import com.revolut.danieledmonds.dao.Database;
import com.revolut.danieledmonds.domain.Account;
import com.revolut.danieledmonds.domain.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PaymentValidationService {

    private final static Logger LOG = LoggerFactory.getLogger(PaymentValidationService.class.getCanonicalName());

    private Database database;

    @Inject
    public PaymentValidationService(Database database) {
        this.database = database;
    }


    public void validatePayment(Payment payment) {

        LOG.info("Validating payload");

        if(payment == null) {
            throw new IllegalArgumentException("Request must contain payment payload in body");
        }

        if(!Long.toString(payment.getAmount()).matches("[0-9]{1,10}")) {
            throw new IllegalArgumentException("Field 'amount' should be a positive integer of maximum 10 digits");
        }

        if(payment.getDebitingAccountNumber() == null || payment.getDebitingAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Field 'debitingAccountNumber' field should be populated");
        }

        if(payment.getCreditingAccountNumber() == null ||payment.getCreditingAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Field 'creditingAccountNumber' field should be populated");
        }

        Account creditingAccount = database.getAccount(payment.getCreditingAccountNumber());

        if (creditingAccount == null) {
            throw new IllegalArgumentException(String.format("Unable to find 'creditingAccountNumber': %s", payment.getCreditingAccountNumber()));
        }

        Account debitingAccount = database.getAccount(payment.getDebitingAccountNumber());

        if (debitingAccount == null) {
            throw new IllegalArgumentException(String.format("Unable to find 'debitingAccountNumber': %s", payment.getCreditingAccountNumber()));
        }

        if (payment.getAmount() > debitingAccount.getAccountBalance()){
            throw new IllegalArgumentException(String.format("Insufficient funds in debiting account '%s'", payment.getDebitingAccountNumber()));
        }
    }
}
