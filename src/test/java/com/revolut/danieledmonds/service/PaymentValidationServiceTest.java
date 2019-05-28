//package com.revolut.danieledmonds.service;
//
//import com.revolut.danieledmonds.dao.AccountDao;
//import com.revolut.danieledmonds.domain.Account;
//import com.revolut.danieledmonds.domain.Payment;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.sql.SQLException;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class PaymentValidationServiceTest {
//
//    @Mock
//    AccountDao accountDao;
//
//    private PaymentValidationService paymentValidator;
//
//    private Account account() {
//        Account account = new Account();
//        account.setAccountNumber("11111");
//        account.setAccountBalance(10000000);
//        return account;
//    }
//
//    private Payment payment() {
//        Payment payment = new Payment();
//        payment.setAmount(-1000000);
//        payment.setCreditingAccountNumber("11111");
//        payment.setDebitingAccountNumber("22222");
//        return payment;
//    }
//
//    @Test
//    public void invalidAmountFails() throws SQLException {
//        paymentValidator = new PaymentValidationService(accountDao);
//
//        when(accountDao.getAccount(anyString())).thenReturn(account());
//
//        try {
//            paymentValidator.validatePayment(payment());
//            throw new Exception();
//        } catch (Exception e){
//            assertEquals("Field 'amount' should be a positive integer of maximum 10 digits", e.getMessage());
//        }
//    }
//
//    @Test
//    public void emptyDebitAccountNumberFails() throws SQLException {
//        paymentValidator = new PaymentValidationService(accountDao);
//
//        when(accountDao.getAccount(anyString())).thenReturn(account());
//
//        Payment payment = payment();
//        payment.setAmount(10000);
//        payment.setCreditingAccountNumber("erer");
//        payment.setDebitingAccountNumber(null);
//
//        try {
//            paymentValidator.validatePayment(payment);
//            throw new Exception();
//        } catch (Exception e){
//            assertEquals("Field 'debitingAccountNumber' field should be populated", e.getMessage());
//        }
//    }
//
//    @Test
//    public void emptyCreditAccountNumberFails() throws SQLException {
//        paymentValidator = new PaymentValidationService(accountDao);
//
//        when(accountDao.getAccount(anyString())).thenReturn(account());
//
//        Payment payment = payment();
//        payment.setAmount(10000);
//        payment.setCreditingAccountNumber(null);
//        payment.setDebitingAccountNumber("22222");
//
//        try {
//            paymentValidator.validatePayment(payment);
//            throw new Exception();
//        } catch (Exception e){
//            assertEquals("Field 'creditingAccountNumber' field should be populated", e.getMessage());
//        }
//    }
//
//    @Test
//    public void getCreditingAccountFails() throws SQLException {
//        paymentValidator = new PaymentValidationService(accountDao);
//
//        when(accountDao.getAccount(anyString())).thenReturn(null);
//
//        Payment payment = payment();
//        payment.setAmount(10000);
//        payment.setCreditingAccountNumber("11111");
//        payment.setDebitingAccountNumber("22222");
//
//        try {
//            paymentValidator.validatePayment(payment);
//            throw new Exception();
//        } catch (Exception e){
//            assertEquals("Unable to find 'creditingAccountNumber': 11111", e.getMessage());
//        }
//    }
//
//    @Test
//    public void getDebitingAccountFails() throws SQLException {
//        paymentValidator = new PaymentValidationService(accountDao);
//
//        when(accountDao.getAccount(anyString())).thenReturn(account()).thenReturn(null);
//
//        Payment payment = payment();
//        payment.setAmount(10000);
//        payment.setCreditingAccountNumber("11111");
//        payment.setDebitingAccountNumber("22222");
//
//        try {
//            paymentValidator.validatePayment(payment);
//            throw new Exception();
//        } catch (Exception e){
//            assertEquals("Unable to find 'debitingAccountNumber': 11111", e.getMessage());
//        }
//    }
//
//    @Test
//    public void insificientFundsCheck() throws SQLException {
//        paymentValidator = new PaymentValidationService(accountDao);
//
//        Account account = new Account();
//        account.setAccountNumber("22222");
//        account.setAccountBalance(100);
//
//        when(accountDao.getAccount(anyString())).thenReturn(account);
//        Payment payment = payment();
//        payment.setAmount(10000);
//        payment.setCreditingAccountNumber("11111");
//        payment.setDebitingAccountNumber("22222");
//
//        try {
//            paymentValidator.validatePayment(payment);
//            throw new Exception();
//        } catch (Exception e){
//            assertEquals("Insufficient funds in debiting account '22222'", e.getMessage());
//        }
//    }
//
//}
