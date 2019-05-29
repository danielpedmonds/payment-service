package com.revolut.danieledmonds.dao;

import com.revolut.danieledmonds.domain.Account;
import com.revolut.danieledmonds.domain.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final static Logger LOG = LoggerFactory.getLogger(Database.class.getCanonicalName());

    private static Connection conn;
    private static boolean initialised = false;

    public void initialiseDatabase() throws SQLException {
        if(!initialised){
            try {
                LOG.info("About to create database connection");
                Class.forName("org.h2.Driver");
                conn = DriverManager.getConnection("jdbc:h2:~/Transfer", "username", "password");
                LOG.info("Database connection established");
            } catch (ClassNotFoundException e) {
                LOG.info("Database connection failed");
                e.printStackTrace();
                System.exit(0);
            }
            createAccountsTable();
            createTransactionsTable();
            insertIntoAccounts(1, 40000);
            insertIntoAccounts(2, 40000);
            initialised = true;
        }

    }

    private void createAccountsTable() throws SQLException {
        LOG.info("Creating ACCOUNTS table");
        PreparedStatement statement = conn.prepareStatement(
                "CREATE TABLE ACCOUNTS " +
                        "(ACCOUNT_NUMBER INTEGER NOT NULL AUTO_INCREMENT," +
                        "BALANCE INTEGER NOT NULL DEFAULT 0," +
                        "CONSTRAINT chk_balance CHECK (BALANCE >= 0)," +
                        "PRIMARY KEY (ACCOUNT_NUMBER))");
        statement.executeUpdate();
    }

    private void createTransactionsTable() throws SQLException {
        LOG.info("Creating TRANSACTIONS table");
        PreparedStatement statement = conn.prepareStatement(
                "CREATE TABLE TRANSACTIONS"+
                        "( TRANSACTION_ID INTEGER AUTO_INCREMENT," +
                        "STATUS VARCHAR2 NOT NULL," +
                        "DEBITING_ACCOUNT_ID VARCHAR2 NOT NULL," +
                        "CREDITING_ACCOUNT_ID VARCHAR2 NOT NULL," +
                        "AMOUNT INTEGER NOT NULL," +
                        "CONSTRAINT constraint_check CHECK (AMOUNT > 0)," +
                        "PRIMARY KEY (TRANSACTION_ID) )");
        statement.executeUpdate();
    }

    public static void terminateConnection() throws SQLException {
        conn.close();
    }

    /**
     * Inserts new account with accountNumber and startingBalance
     * @param accountNumber
     * @param balance
     * @throws SQLException
     */
    public void insertIntoAccounts(int accountNumber, double balance) throws SQLException {

        LOG.info(String.format("Inserting account accountNumber: %s, balance: %s", accountNumber, balance));
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO ACCOUNTS VALUES (?,?)");
            statement.setInt(1, accountNumber);
            statement.setDouble(2, balance);
            statement.executeUpdate();
            statement.close();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the account number and balance for a given account number.
     * @param accountNumber
     * @return Account object
     */
    public Account getAccount(String accountNumber) {
        LOG.info(String.format("About to retrieve account with accountNumber:  %s", accountNumber));
        try {
            Account account = new Account();
            PreparedStatement statement = conn.prepareStatement("SELECT ACCOUNT_NUMBER, BALANCE from ACCOUNTS WHERE ACCOUNT_NUMBER=?");
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                account.setAccountNumber(resultSet.getString(1));
                account.setAccountBalance(resultSet.getInt(2));
            }
            return account;
        } catch (SQLException e) {
            LOG.info("Error occurred retrieving account with account number: {}", accountNumber);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * This method creates a new transaction with the status NOT_PROCESSED, returning the Id of the transaction inserted
     * @param debitingAccount
     * @param creditingAccount
     * @param amount
     * @return long transactionId
     * @throws SQLException
     */
    public long insertTransaction(String debitingAccount, String creditingAccount, long amount) throws SQLException {
        long transactionId;
        conn.setAutoCommit(false);

        PreparedStatement insertTransactionStatement = conn.prepareStatement("INSERT INTO TRANSACTIONS (STATUS, DEBITING_ACCOUNT_ID, CREDITING_ACCOUNT_ID, AMOUNT) VALUES ( ?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        insertTransactionStatement.setString(1, TransactionStatus.NOT_PROCESSED.name());
        insertTransactionStatement.setString(2, debitingAccount);
        insertTransactionStatement.setString(3, creditingAccount);
        insertTransactionStatement.setLong(4, amount);
        insertTransactionStatement.executeUpdate();

        ResultSet resultSet = insertTransactionStatement.getGeneratedKeys();
        if (resultSet.next()) {
            transactionId = resultSet.getLong(1);
        } else {
            throw new SQLException("No transaction ID generate for transaction");
        }
        conn.commit(); //transaction block end
        return transactionId;
    }

    /**
     * This method attempts to update the status of a transaction to PROCESSED for a given transaction ID,
     * followed by updating the creditingAccount and then the debitingAccount in one atomic action,
     * such that if any of the updates fail the queries get rolled back and the transaction remains NOT_PROCESSED
     * @param debitingAccount
     * @param creditingAccount
     * @param amount
     * @return long transactionId
     */
    public void updateTransactionStatusAndAccountBalance(String debitingAccount, String creditingAccount, long amount, long transactionId) throws SQLException {
        conn.setAutoCommit(false); //transaction block start

        PreparedStatement insertTransactionStatement = conn.prepareStatement("UPDATE TRANSACTIONS SET STATUS = ? WHERE TRANSACTION_ID = ?");
        insertTransactionStatement.setString(1, TransactionStatus.PROCESSED.name());
        insertTransactionStatement.setLong(2, transactionId);
        insertTransactionStatement.executeUpdate();

        PreparedStatement updateCreditingAccountStatement = conn.prepareStatement("UPDATE ACCOUNTS SET BALANCE=BALANCE+? WHERE ACCOUNT_NUMBER=?");
        updateCreditingAccountStatement.setLong(1, amount);
        updateCreditingAccountStatement.setString(2, creditingAccount);
        updateCreditingAccountStatement.executeUpdate();

        PreparedStatement updateDebitingAccountStatement = conn.prepareStatement("UPDATE ACCOUNTS SET BALANCE=BALANCE-? WHERE ACCOUNT_NUMBER=?");
        updateDebitingAccountStatement.setLong(1, amount);
        updateDebitingAccountStatement.setString(2, debitingAccount);
        updateDebitingAccountStatement.executeUpdate();

        conn.commit(); //transaction block end
    }
}
