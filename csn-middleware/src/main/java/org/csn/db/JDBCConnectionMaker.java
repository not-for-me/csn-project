package org.csn.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class JDBCConnectionMaker implements ConnectionMaker {

	public Connection makeConnection() throws ClassNotFoundException,
			SQLException, IOException {
		Class.forName("com.mysql.jdbc.Driver");

		Map<String, String> confMap = DBConfiguration.getDBConfMap();

		return DriverManager.getConnection(confMap.get("mysql-url"),
				confMap.get("mysql-user"), confMap.get("mysql-password"));
	}

}
