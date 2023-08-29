package com.code.auditor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class DatabaseCreationService {

    @Autowired
    private DataSource dataSource;

    public void createDatabaseIfNotExists(String databaseName) {
        String checkSql = "SELECT 1 FROM pg_database WHERE datname = ?";
        String createSql = "CREATE DATABASE " + databaseName;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, databaseName);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {  // Database does not exist
                    try (Statement createStmt = conn.createStatement()) {
                        createStmt.execute(createSql);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking or creating database", e);
        }
    }
}
