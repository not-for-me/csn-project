package org.csn.component.threads;

import org.csn.data.SensorData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class CentralizedDataSubscriber extends Thread implements MqttCallback{
    Logger logger = LoggerFactory.getLogger(CentralizedDataSubscriber.class);

    protected BlockingQueue queue;
    private MqttClient myClient;
    private ObjectMapper jsonMapper;

    private static final boolean MQTT_CLEAN_SESSION_OPT = true;
    private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
    private static final int MQTT_QOS_OPT = 1;
    private static final String MQTT_BROKER_URL_OPT = "tcp://localhost:1883";
    private static final String MQTT_CLIENT_ID = "Subscriber-Node";
    private static final String MQTT_SUBS_TOPIC = "CSN/CENTRAL/DATA";

    private volatile boolean stopped = false;

    public CentralizedDataSubscriber(String name, BlockingQueue queue) {
        super(name);
        this.queue = queue;
        jsonMapper = new ObjectMapper();
    }


    @Override
    public void run() {
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

        // Setup topic
        // topics on m2m.io are in the form <domain>/<stuff>/<thing>

        // Subscribe to topic if subscriber
        try {
            myClient.subscribe(MQTT_SUBS_TOPIC, MQTT_QOS_OPT);

            while( !isStopped() ){
            }
            logger.info("Disconnecting to MQ");
            myClient.disconnect();
            logger.info("Thread will be stopped");
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        // TODO Need to be implemented for Connection Lost Error
        logger.warn("Connection lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String data = mqttMessage.toString();
        logger.info("MQTT MSG Come: {}", data);
        // TODO Implement to parse JSON-STRING
        SensorData sensorData = jsonMapper.readValue(data, SensorData.class);
        try {
            queue.put(sensorData);
        } catch (InterruptedException e) {
            logger.warn("System is interrupted");
        }
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
