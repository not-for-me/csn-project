package org.csn.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnectionMaker implements ConnectionMaker {
    private static String DB_PATH = "jdbc:mysql://localhost:3306/csn";
    private static String DB_USER = "root";
    private static String DB_PW = "cir@817";

    public Connection makeConnection() throws ClassNotFoundException, SQLException, IOException {
//        Properties pro = new Properties();
//        pro = this.readProperties("src/main/resources/db.properties");
        Class.forName("com.mysql.jdbc.Driver");
        Connection c  = DriverManager.getConnection(DB_PATH,DB_USER, DB_PW);
        return c;
    }

//    public Properties readProperties(String filename) {
//        Properties pro = new Properties();
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(filename);
//            pro.load(new InputStreamReader(fis));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return pro;
//    }
}
