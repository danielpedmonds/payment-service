package com.revolut.danieledmonds.service;

import com.revolut.danieledmonds.api.PaymentApi;
import com.revolut.danieledmonds.domain.Payment;
import com.revolut.danieledmonds.validation.PaymentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PaymentService {

    private PaymentValidator paymentValidator;

    private final static Logger LOG = LoggerFactory.getLogger(PaymentApi.class.getCanonicalName());

    @Inject
    public PaymentService(PaymentValidator paymentValidator) {
        this.paymentValidator = paymentValidator;
    }

    public boolean processPayment(Payment payment) {

        paymentValidator.validatePayment(payment);


        return true;
    }

}
