package org.csn.subscriber.impl;

import org.csn.subscriber.MessageCallback;
import org.csn.subscriber.TopicSubscriber;
import org.eclipse.paho.client.mqttv3.*;

public class TopicSubscriberImpl implements MqttCallback, TopicSubscriber {

    private static final boolean MQTT_CLEAN_SESSION_OPT = true;
    private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
    private static final int MQTT_QOS_OPT = 1;
    private MqttClient myClient;
    private MessageCallback callback;
    private String domain;
    private String appName;
    private String topicName;


    @Override
    public void setConnection(String domain, String appName, String topicName) {
        this.domain = domain;
        this.appName = appName;
        this.topicName = topicName;
    }

    @Override
    public void setMessageCallback(MessageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void subscribe() {
        // setup MQTT Client
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(MQTT_CLEAN_SESSION_OPT);
        connOpt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_OPT);
        // Connect to Broker
        try {
            myClient = new MqttClient(domain, appName);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // Subscribe to topic if subscriber
        try {
            System.out.println("Start to subscribe to " + topicName);
            myClient.subscribe(topicName, MQTT_QOS_OPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe() {
        try {
            myClient.disconnect();
            System.out.println("Disconneted to " + topicName);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        callback.connectionLost(cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String data = message.toString();
        callback.messageArrived(data);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        callback.deliveryComplete(token);
    }
}
