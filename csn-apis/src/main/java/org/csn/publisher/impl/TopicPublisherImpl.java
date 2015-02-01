package org.csn.publisher.impl;

import org.csn.client.SensorData;
import org.csn.publisher.TopicPublisher;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopicPublisherImpl implements MqttCallback, TopicPublisher {
	private static final boolean MQTT_CLEAN_SESSION_OPT = true;
	private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
	private static final int MQTT_QOS_OPT = 1;
	private MqttClient myClient;
	private MqttTopic topic;
	private String clientID;
	private String brokerURL;
	private String topicPath;

	@Override
	public void setConnection(String domain, String appName, String topicName) {
		brokerURL = domain;
		clientID = appName;
		topicPath = topicName;
	}

	@Override
	public void connect() {
		// Setup MQTT Client
		MqttConnectOptions connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(MQTT_CLEAN_SESSION_OPT);
		connOpt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_OPT);

		// Connect to Broker
		try {
			myClient = new MqttClient(brokerURL, clientID);
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		// Setup topic
		topic = myClient.getTopic(topicPath);
	}

	@Override
	public void close() {
		try {
			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String convertToString(Object data) {
		ObjectMapper jsonMapper = new ObjectMapper();
		String jsonStr = null;;
		try {
			jsonStr = jsonMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	private void publish(String jsonStr) {
		MqttMessage message = new MqttMessage(jsonStr.getBytes());
		message.setQos(MQTT_QOS_OPT);
		message.setRetained(false);
		MqttDeliveryToken token = null;
		try {
			token = topic.publish(message);
			token.waitForCompletion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void publish(String id, String timestamp, String value) {
		String jsonStr = convertToString(new SensorData(id, timestamp, value));
		publish(jsonStr);
	}

	@Override
	public void publish(Object data) {
		String jsonStr = convertToString(data);
		publish(jsonStr);
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost!");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
	}

}
