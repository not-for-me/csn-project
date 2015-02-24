package org.csn.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.csn.util.CSNXMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Map;

public class MongoDBConnectionMaker {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private MongoClient mongoClient;
	private DB db;

	public MongoDBConnectionMaker() {
		try {
			mongoClient = new MongoClient(
					DBConfiguration.DB_CONF_MAP.get("mongo-url"),
					Integer.parseInt(DBConfiguration.DB_CONF_MAP
							.get("mongo-port")));
			db = mongoClient.getDB(DBConfiguration.DB_CONF_MAP
					.get("mongo-db-name"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public DB getMongoDB() {
		return this.db;
	}

	public void closeMongo() {
		mongoClient.close();
	}
}
