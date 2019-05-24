package com.revolut.danieledmonds.dao;

import com.revolut.danieledmonds.api.PaymentApi;
import com.revolut.danieledmonds.database.Database;
import com.revolut.danieledmonds.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {

    private final static Logger LOG = LoggerFactory.getLogger(PaymentApi.class.getCanonicalName());

    public boolean checkAccountExists(String accountNumber) {
        try {
            LOG.info("About to perform 'checkAccountExists' query");
            PreparedStatement statement = Database.getStatement("SELECT COUNT(*) FROM ACCOUNTS WHERE ACCOUNT_NUMBER=?");
            statement.setString(1, accountNumber);
            ResultSet count = Database.selectStatement(statement);
            boolean exists = false;
            if (count.next()) {
                exists = count.getInt(1) == 1;
            }
            statement.close();
            return exists;
        } catch (SQLException e) {
            LOG.info("Error occurred checking for account with account number: {}", accountNumber);
            e.printStackTrace();
            return false;
        }
    }

    public Account getAccount(String accountNumber) {
        try {
            Account account = new Account();
            PreparedStatement statement = Database.getStatement("SELECT 'ACCOUNT_NUMBER' from ACCOUNTS WHERE ACCOUNT_NUMBER=?");
            statement.setString(1, accountNumber);
            ResultSet resultSet = Database.selectStatement(statement);
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
}
