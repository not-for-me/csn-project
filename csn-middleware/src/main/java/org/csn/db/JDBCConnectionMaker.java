package org.csn.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.csn.util.LogPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCConnectionMaker implements ConnectionMaker {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JDBCConnectionMaker.class.getClass());

	public Connection makeConnection() throws ClassNotFoundException,
			SQLException, IOException {
		Class.forName("com.mysql.jdbc.Driver");

		Map<String, String> confMap = DBConfiguration.getDBConfMap();
		LOGGER.info("DB Connection Info: {}", confMap);
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/csn",
				"root", "1234");
	}

}
