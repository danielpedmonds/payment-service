package com.revolut.danieledmonds.validation;

import com.google.gson.Gson;
import com.revolut.danieledmonds.domain.Payment;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

public class PaymentValidatorTest {

    PaymentValidator paymentValidator = new PaymentValidator();

    @Test
    public void validateAmount() {

        Payment payment = new Payment();
        payment.setAmount(-10000000);
        payment.setCreditingAccountNumber("11111");
        payment.setDebitingAccountNumber("222222");

        paymentValidator.validatePayment(payment);

    }
}
