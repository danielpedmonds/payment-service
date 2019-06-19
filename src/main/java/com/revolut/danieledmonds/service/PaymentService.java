package com.revolut.danieledmonds.service;

import com.revolut.danieledmonds.api.PaymentApi;
import com.revolut.danieledmonds.dao.Database;
import com.revolut.danieledmonds.domain.Payment;
import com.revolut.danieledmonds.domain.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.sql.SQLException;

public class PaymentService {

    private PaymentValidationService paymentValidator;

    private Database database;

    private final static Logger LOG = LoggerFactory.getLogger(PaymentApi.class.getCanonicalName());

    @Inject
    public PaymentService(PaymentValidationService paymentValidator, Database database) {
        this.paymentValidator = paymentValidator;
        this.database = database;
    }

    public Response processPayment(Payment payment) {
        try {
            LOG.info("About to process parsed payment payload");

            try {
                paymentValidator.validatePayment(payment);
            } catch (IllegalArgumentException e) {
                return new Response(HttpStatus.BAD_REQUEST_400, String.format("Invalid arguments passed: %s", e.getMessage()));
            }

            long transactionId = database.insertTransaction(
                    payment.getDebitingAccountNumber(),
                    payment.getCreditingAccountNumber(),
                    payment.getAmount());

            try {
                database.updateTransactionStatusAndAccountBalance(
                        payment.getDebitingAccountNumber(),
                        payment.getCreditingAccountNumber(),
                        payment.getAmount(),
                        transactionId);
            } catch (SQLException e) {
                LOG.info("Error occurred processing transaction with ID '{}': {}", transactionId, e);
                return new Response(HttpStatus.INTERNAL_SERVER_ERROR_500,
                        String.format("Transaction with ID: '%s' was logged but failed to be processed", transactionId));
            }

            return new Response(HttpStatus.OK_200, String.format("Transaction number '%s' has been inserted", transactionId));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR_500, String.format("Transaction failed to be processed: %s", e.getMessage()));
        }
    }

}
