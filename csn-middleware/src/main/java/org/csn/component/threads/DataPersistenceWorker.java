package org.csn.component.threads;

import org.csn.db.MongoDBConnectionMaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPersistenceWorker extends Thread implements MqttCallback {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MqttClient myClient;
    private ObjectMapper jsonMapper;
    private MongoDBConnectionMaker mongoConnMaker;
    private DB mongo;

    private static final boolean MQTT_CLEAN_SESSION_OPT = true;
    private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
    private static final int MQTT_QOS_OPT = 1;
    private static final String MQTT_BROKER_URL_OPT = "tcp://localhost:1883";
    private static final String MQTT_CLIENT_ID = "Persistence-Node";
    static final String HISTORICAL_DATA_TOPIC = "CSN/SINGLE/>";

    private volatile boolean stopped = false;

    public DataPersistenceWorker(String name) {
        super(name);
        jsonMapper = new ObjectMapper();
        mongoConnMaker = new MongoDBConnectionMaker();
        mongo = mongoConnMaker.getMongoDB();
    }

    public void before() throws Exception {
        logger.info("Connecting to Messaging Queue");

        // setup MQTT Client
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(MQTT_CLEAN_SESSION_OPT);
        connOpt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_OPT);

        // Connect to Broker
        try {
            myClient = new MqttClient(MQTT_BROKER_URL_OPT, MQTT_CLIENT_ID);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        logger.info("Connected to {}", MQTT_BROKER_URL_OPT);
    }

    @Override
    public void run() {
        try {
            before();
            myClient.subscribe(HISTORICAL_DATA_TOPIC, MQTT_QOS_OPT);
            while( !isStopped() ) {
            }
            logger.info("Disconnecting to MQ");
            mongoConnMaker.closeMongo();
            myClient.disconnect();
            logger.info("Thread will be stopped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable throwable) {
        logger.warn("Connection lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String data = mqttMessage.toString();
        logger.info("Persistence Data: {}", data);
        DBCollection userTable = mongo.getCollection("SensorData");
        DBObject dbObject = (DBObject) JSON.parse(data);
        userTable.insert(dbObject);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
