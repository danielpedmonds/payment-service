package com.revolut.danieledmonds.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.danieledmonds.HelloMessage;
import com.revolut.danieledmonds.HelloMessageService;
import com.revolut.danieledmonds.dao.AccountDao;
import com.revolut.danieledmonds.database.Database;
import com.revolut.danieledmonds.domain.Payment;
import com.revolut.danieledmonds.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;

import javax.inject.Inject;

import static spark.Spark.get;

public class PaymentApi {

    private final PaymentService paymentService;
    private final AccountDao accountDao;

    private final static Logger LOG = LoggerFactory.getLogger(PaymentApi.class.getCanonicalName());

    @Inject
    PaymentApi(final PaymentService paymentService, AccountDao accountDao) {
        this.paymentService = paymentService;
        this.accountDao = accountDao;
    }

    public void payment() {
        get("/payment","application/json", (request, response) -> {

            Payment payment = new Gson().fromJson(request.body(), Payment.class);

            paymentService.processPayment(payment);

            //boolean abc = accountDao.exists(1);

            return "";
        });
    }

}
