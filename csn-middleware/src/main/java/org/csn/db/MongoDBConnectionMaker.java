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
        String filePath =  this.getClass().getClassLoader().getResource("").getPath() + "../configuration.xml";
        Map<String, String> connInfoMap = CSNXMLParser.getPersitentDBConnInfo(filePath);
        try {
            mongoClient = new MongoClient(connInfoMap.get("url"), Integer.parseInt(connInfoMap.get("port")));
            db = mongoClient.getDB(connInfoMap.get("dbName"));
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
