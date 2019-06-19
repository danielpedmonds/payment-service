package com.revolut.danieledmonds.service;

import com.revolut.danieledmonds.dao.Database;
import com.revolut.danieledmonds.domain.Payment;
import com.revolut.danieledmonds.domain.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.sql.SQLException;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceUnitTest {

    @Mock
    private Database database;

    @Mock
    private PaymentValidationService paymentValidationService;

    private PaymentService paymentService;

    private Payment payment() {
        Payment payment = new Payment();
        payment.setAmount(10000);
        payment.setCreditingAccountNumber("11111");
        payment.setDebitingAccountNumber("22222");
        return payment;
    }

    @Test
    public void processPaymentSuccessfulResponse() throws SQLException {

        paymentService = new PaymentService(paymentValidationService, database);

        when(database.insertTransaction(anyString(), anyString(), anyLong())).thenReturn(1L);

        Response response = paymentService.processPayment(payment());

        assertEquals("Transaction number '1' has been inserted", response.getBody());
        assertEquals(200, response.getCode());
    }

    @Test
    public void processPaymentFailsValidationResultingInFailureResponse() throws SQLException {

        paymentService = new PaymentService(paymentValidationService, database);

        doThrow(new IllegalArgumentException("Request must contain payment payload in body")).when(paymentValidationService).validatePayment(any(Payment.class));
        paymentValidationService.validatePayment(any(Payment.class));

        Response response = paymentService.processPayment(payment());

        assertEquals("Invalid arguments passed: Request must contain payment payload in body", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST_400, response.getCode());
    }

    @Test
    public void updateTransactionStatusAndAccountBalanceResultingInFailureResponse() throws SQLException {

        paymentService = new PaymentService(paymentValidationService, database);

        when(database.insertTransaction(anyString(), anyString(), anyLong())).thenReturn(1L);

        doThrow(new SQLException("Something went wrong inserting"))
                .when(database)
                .updateTransactionStatusAndAccountBalance(anyString(), anyString(), anyLong(), anyLong());

        Response response = paymentService.processPayment(payment());

        assertEquals("Transaction with ID: '1' was logged but failed to be processed", response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getCode());
    }

    @Test
    public void processPaymentFailureResponse() throws SQLException {

        paymentService = new PaymentService(paymentValidationService, database);

        when(database.insertTransaction(anyString(), anyString(), anyLong())).thenThrow(new SQLException("Some problem"));

        Response response = paymentService.processPayment(payment());

        assertEquals("Transaction failed to be processed: Some problem", response.getBody());
        assertEquals(500, response.getCode());

    }
}
