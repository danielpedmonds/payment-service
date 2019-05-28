package com.revolut.danieledmonds.api;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class PaymentApiRestAssuredTest {


    private static final String API_ROOT = "http://localhost:9999";

    private static final String API_PAYMENT = "/payment";

    @BeforeClass
    public static void setup() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("java -jar build/libs/payments-service-1.0.jar");
    }

    @Test
    public void makeSureThatPaymentFailsWithNoBody() {
        given().when()
               .post(API_ROOT + API_PAYMENT)
               .then().statusCode(500);
    }

    @Test
    public void makeSureThatPaymentFailsInValidationWithInvalidRequestBody() {
        given().when()
                .body("{ \"debitingAccountNumber\": \"1\", \"creditingAccountNumber\": \"2\", \"amount\": \"-12\"}")
                .post(API_ROOT + API_PAYMENT)
                .then()
                .statusCode(500)
                .body(equalTo("Transaction failed to be processed: Field 'amount' should be a positive integer of maximum 10 digits"));
    }

    @Test
    public void makeSureThatPaymentFailsInValidationWithInsufficientFunds() {
        given().when()
                .body("{ \"debitingAccountNumber\": \"1\", \"creditingAccountNumber\": \"2\", \"amount\": \"100000\"}")
                .post(API_ROOT + API_PAYMENT)
                .then()
                .statusCode(500)
                .body(equalTo("Transaction failed to be processed: Insufficient funds in debiting account '1'"));
    }

    @Test
    public void makeSureThatPaymentPassesWithValidPayload() {
        given().when()
               .body("{ \"debitingAccountNumber\": \"1\", \"creditingAccountNumber\": \"2\", \"amount\": \"12\"}")
               .post(API_ROOT + API_PAYMENT)
               .then()
               .statusCode(200)
               .body(containsString(" has been inserted"));
    }
}
