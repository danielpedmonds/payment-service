package com.revolut.danieledmonds.dao;

import com.revolut.danieledmonds.api.PaymentApi;
import com.revolut.danieledmonds.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {

    private final static Logger LOG = LoggerFactory.getLogger(PaymentApi.class.getCanonicalName());

    public boolean checkAccountExists(int userId) throws SQLException {
        LOG.info("About to perform 'checkAccountExists' query");
        PreparedStatement statement = Database.getStatement("SELECT COUNT(*) FROM ACCOUNTS WHERE ACCOUNT_NUMBER=?");
        statement.setInt(1, userId);
        ResultSet count = Database.selectStatement(statement);
        boolean exists = false;
        if(count.next()) {
            exists = count.getInt(1) == 1;
        }
        statement.close();
        return exists;
    }
}
