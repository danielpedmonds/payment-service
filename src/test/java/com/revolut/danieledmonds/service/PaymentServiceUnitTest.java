package com.revolut.danieledmonds.service;

import com.revolut.danieledmonds.domain.Payment;
import com.revolut.danieledmonds.domain.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceUnitTest {


    private PaymentService paymentService;

    private Payment payment() {
        Payment payment = new Payment();
        payment.setAmount(10000);
        payment.setCreditingAccountNumber("11111");
        payment.setDebitingAccountNumber("22222");
        return payment;
    }

    @Test
    public void processPaymentSuccess() {

        Response response = paymentService.processPayment(any(Payment.class));

        assertEquals("Field 'debitingAccountNumber' field should be populated", response);

    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
