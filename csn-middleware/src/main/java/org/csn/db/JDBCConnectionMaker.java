package org.csn.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectionMaker implements ConnectionMaker {
    private static String DB_PATH = "jdbc:mysql://localhost:3306/csn";
    private static String DB_USER = "root";
    private static String DB_PW = "cir@817";

    public Connection makeConnection() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c  = DriverManager.getConnection(DB_PATH,DB_USER, DB_PW);
        return c;
    }


}
