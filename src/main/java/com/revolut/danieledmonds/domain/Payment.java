package com.revolut.danieledmonds.domain;

public class Payment {

    private String debitingAccountNumber;

    private String creditingAccountNumber;

    private int amount;


    public String getDebitingAccountNumber() {
        return debitingAccountNumber;
    }

    public void setDebitingAccountNumber(String debitingAccountNumber) {
        this.debitingAccountNumber = debitingAccountNumber;
    }

    public String getCreditingAccountNumber() {
        return creditingAccountNumber;
    }

    public void setCreditingAccountNumber(String creditingAccountNumber) {
        this.creditingAccountNumber = creditingAccountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
