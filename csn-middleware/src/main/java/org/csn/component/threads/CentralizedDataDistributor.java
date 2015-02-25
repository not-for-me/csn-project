package org.csn.component.threads;

import org.csn.data.NetworkMappingMap;
import org.csn.data.SensorData;
import org.csn.util.LogPrinter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class CentralizedDataDistributor extends Thread implements MqttCallback {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CentralizedDataDistributor.class.getClass());

	MqttClient myClient;
	MqttConnectOptions connOpt;

	protected BlockingQueue<SensorData> queue;
	static final String CLIENT_ID = "Publisher-Node";
	static final String BROKER_URL = "tcp://localhost:1883";

	private volatile boolean stopped = false;

	public CentralizedDataDistributor(String name,
			BlockingQueue<SensorData> queue) {
		super(name);
		this.queue = queue;
	}

	private void openMQTTConn() throws Exception {
		LOGGER.info("Connecting to Messaging Queue");

		// Setup MQTT Client
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);

		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, CLIENT_ID);
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
			System.exit(-1);
		}

		LOGGER.info("Connected to " + BROKER_URL);
	}

	public void closeMQTTconn() throws Exception {
		LOGGER.info("Closing Connection");

		// disconnect
		try {
			myClient.disconnect();
			System.out.println("Close Connection");
		} catch (Exception e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
		}
	}

	public void connectionLost(Throwable t) {
		System.out.println("Connection lost!");
	}

	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void run() {
		try {
			openMQTTConn();
			while (!isStopped()) {
				publishData();
			}
			closeMQTTconn();
			LOGGER.info("Thread will be stopped");
		} catch (Exception e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
		}
	}

	private void publishData() {

		if (queue.peek() != null) {
			LOGGER.info("Pop Sensor Data from Central Queue ...");
			SensorData sensorData = queue.poll();

			Set<String> topicPaths = NetworkMappingMap.getMappingMap().get(
					sensorData.getId());

			if (topicPaths != null) {
				distributeSnsrData(sensorData, topicPaths);
			} else {
				LOGGER.info("Data ID: {} is not registered in the CSN",
						sensorData.getId());
			}
		}
	}

	
	private void distributeSnsrData(SensorData sensorData,
			Set<String> topicPaths) {
		LOGGER.info("Data ID: {} is registered in the CSN", sensorData.getId());
		for (String topicPath : topicPaths) {
			LOGGER.info("Current Sensor Data is a member of {}", topicPath);

			String mqttTopicName = topicPath.replace('.', '/');
			// Setup topic
			MqttTopic topic = myClient.getTopic(mqttTopicName);
			ObjectMapper jsonMapper = new ObjectMapper();

			String jsonStr = null;
			try {
				jsonStr = jsonMapper.writeValueAsString(sensorData);
			} catch (JsonProcessingException e) {
				LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
						e.getMessage());
			}
			LOGGER.trace("RePublished MSG: {}", jsonStr);

			int pubQoS = 0;
			MqttMessage message = new MqttMessage(jsonStr.getBytes());
			message.setQos(pubQoS);
			message.setRetained(false);
			MqttDeliveryToken token = null;
			try {
				// publish message to broker
				token = topic.publish(message);
				// Wait until the message has been delivered to the
				// broker
				token.waitForCompletion();
			} catch (Exception e) {
				LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
						e.getMessage());
			}
		}
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
}
