package com.revolut.danieledmonds.database;

import com.google.inject.AbstractModule;
import com.revolut.danieledmonds.Application;
import com.revolut.danieledmonds.dao.AccountDao;
import com.revolut.danieledmonds.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private final static Logger LOG = LoggerFactory.getLogger(Database.class.getCanonicalName());

    private static Connection conn;
    private static boolean initialised = false;

    private static final String createAccountTableExpr =
            "CREATE TABLE ACCOUNTS"+
                    "( ACCOUNT_NUMBER INTEGER NOT NULL AUTO_INCREMENT," +
                    "Balance INTEGER NOT NULL DEFAULT 0," +
                    "CONSTRAINT chk_balance CHECK (Balance >= 0)," +
                    "PRIMARY KEY (UserId))";
    private static final String createTransactionTableExpr =
            "CREATE TABLE TRANSACTIONS"+
            "( TransactionId INTEGER NOT NULL," +
            "State INTEGER NOT NULL DEFAULT 0," +
            "Sender INTEGER NOT NULL," +
            "Receiver INTEGER NOT NULL," +
            "Amount DOUBLE NOT NULL," +
            "CONSTRAINT chk_amount CHECK (Amount > 0)," +
            "PRIMARY KEY (TransactionId) )";

    public static void initialiseDatabase() throws SQLException {
        if(!initialised){
            try {
                Class.forName("org.h2.Driver");
                conn = DriverManager.getConnection("jdbc:h2:~/Bank", "username", "password");

                LOG.info("Connection ");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }

            initialised = true;
        }

    }

    public static PreparedStatement getStatement(String query) throws SQLException {
        LOG.info(String.format("Get statement:  %s\n", query));
        return conn.prepareStatement(query);
    }

    public static ResultSet selectStatement(PreparedStatement statement) throws SQLException {
        LOG.info(String.format("Try to execute query:  %s\n", statement));
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.info(String.format("Query executed, returning results: %s\n", resultSet));
        return resultSet;
    }

    public static void terminateConnection() throws SQLException {
        conn.close();
    }
}
