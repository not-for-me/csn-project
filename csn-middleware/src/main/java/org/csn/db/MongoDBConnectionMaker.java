package org.csn.db;

import java.net.UnknownHostException;
import java.util.Map;

import org.csn.util.LogPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBConnectionMaker {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MongoDBConnectionMaker.class.getClass());

	private MongoClient mongoClient;
	private DB mongoDB;

	public MongoDBConnectionMaker() {
		Map<String, String> confMap = DBConfiguration.getDBConfMap();
		LOGGER.info("DB Connection Info: {}", confMap);
		try {
			mongoClient = new MongoClient(confMap.get("mongo-url"),
					Integer.parseInt(confMap.get("mongo-port")));

			mongoDB = mongoClient.getDB(confMap.get("mongo-db-name"));
		} catch (UnknownHostException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
		}
	}

	public DB getMongoDB() {
		return this.mongoDB;
	}

	public void closeMongo() {
		mongoClient.close();
	}
}
