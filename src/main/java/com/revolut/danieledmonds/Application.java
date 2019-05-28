package com.revolut.danieledmonds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.revolut.danieledmonds.api.PaymentApi;
import com.revolut.danieledmonds.dao.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.SQLException;

import static spark.Spark.*;

public class Application {

    private final static Logger LOG = LoggerFactory.getLogger(Application.class.getCanonicalName());

    private final PaymentApi paymentApi;

    private final Database database;

    @Inject
    Application(final PaymentApi paymentApi, final Database database) {
        this.paymentApi = paymentApi;
        this.database = database;
    }

    void run(final int port) {
        port(port);

        try {
            database.initialiseDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        before("/*", (req, res) -> LOG.info(String.format("%s: %s", req.requestMethod(), req.uri())));

        paymentApi.payment();

        after("/*", (req, res) -> LOG.info(res.body()));
    }

    public static void main(final String... args) {
        Guice.createInjector(new GuiceModule())
                .getInstance(Application.class)
                .run(9999);
    }
}
