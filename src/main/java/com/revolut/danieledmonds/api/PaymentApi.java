package com.revolut.danieledmonds.api;

import com.revolut.danieledmonds.domain.Payment;
import com.revolut.danieledmonds.domain.Response;
import com.revolut.danieledmonds.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import javax.inject.Inject;
import static spark.Spark.post;

public class PaymentApi {

    private final PaymentService paymentService;

    private final static Logger LOG = LoggerFactory.getLogger(PaymentApi.class.getCanonicalName());

    @Inject
    PaymentApi(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * /payment endpoint for creating a payment with debitingAccountNumber, creditingAccountNumber & amount
     */
    public void payment() {
        post("/payment","application/json", (request, response) -> {
            LOG.info("/payment endpoint hit, about to call PaymentService");
            Response serviceResponse = paymentService.processPayment(
                    new Gson().fromJson(request.body(), Payment.class));
            response.body(serviceResponse.getBody());
            response.status(serviceResponse.getCode());
            return response;
        });
    }

}
