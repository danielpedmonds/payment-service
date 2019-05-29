package com.revolut.danieledmonds.domain;

public class Payment {

    private String debitingAccountNumber;

    private String creditingAccountNumber;

    private long amount;


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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
