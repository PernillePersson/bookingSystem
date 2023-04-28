package com.example.bookingsystem.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {

    private static ConnectionSingleton instance;

    private Connection connection;

    private ConnectionSingleton() {
        try {
            String serverName = "EASV-DB4";
            String databaseName = "BookingDB_MMP";
            String userName = "CSe2022t_t_1";
            String password = "CSe2022tT1#";

            String url = "jdbc:sqlserver://" + serverName + ":1433;DatabaseName=" + databaseName + ";user=" + userName + ";password=" + password + ";encrypt=false;trustServerCertificate=true;";
            connection = DriverManager.getConnection(url);
            System.out.println("connected to DB");
        } catch (SQLException e) {
            System.err.println("can not create connection");
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectionSingleton getInstance() throws SQLException {

        if (instance == null) {
            instance = new ConnectionSingleton();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConnectionSingleton();
        }

        return instance;
    }
}
