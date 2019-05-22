package com.revolut.danieledmonds.validation;

import com.google.gson.Gson;
import com.revolut.danieledmonds.domain.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentValidator {

    public void validatePayment(Payment payment) {

        List<String> error = new ArrayList<String>();

        //Validate amount is valid value
        if(!Integer.toString(payment.getAmount()).matches("[0-9]{1,10}")) {
            error.add("Invalid amount passed");
        }

        //Get Debiting account

        //Validate debiting account has enough funds

        //Validate crediting account exists


        if(!error.isEmpty()) {
            throw new IllegalArgumentException(new Gson().toJson(error));
        }
    }
}
