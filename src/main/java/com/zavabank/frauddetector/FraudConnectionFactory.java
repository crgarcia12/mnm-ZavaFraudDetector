package com.zavabank.frauddetector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class FraudConnectionFactory {
    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("SQL Server JDBC driver not found", exception);
        }
    }

    private FraudConnectionFactory() {
    }

    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
            FraudConfig.getDbUrl(),
            FraudConfig.getDbUser(),
            FraudConfig.getDbPassword()
        );
    }
}
