package org.csn.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoDBConnectionMaker {
    private MongoClient mongoClient;
    private DB db;


    public MongoDBConnectionMaker() {
        try {
            mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("CSN");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DB getMongoDB() {
        return this.db;
    }
}
