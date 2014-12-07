package org.csn.db;

import org.csn.util.CSNXMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class JDBCConnectionMaker implements ConnectionMaker {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Connection makeConnection() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        String dbPath =  this.getClass().getClassLoader().getResource("").getPath() + "../configuration.xml";
        Map<String, String> connInfoMap = CSNXMLParser.getDBConnInfo(dbPath);

        Connection c = DriverManager.getConnection(
                connInfoMap.get("url"),
                connInfoMap.get("user"),
                connInfoMap.get("password")
        );
        return c;
    }

}
