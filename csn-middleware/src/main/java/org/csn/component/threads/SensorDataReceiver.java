package org.csn.component.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.csn.data.NetworkMappingMap;
import org.csn.data.SensorData;
import org.csn.util.CSNXMLParser;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

public class SensorDataReceiver extends Thread {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<DataAccepter> accepters;
	private volatile boolean stopped;
	private int port;

	public SensorDataReceiver(String name) {
		super(name);
		logger = LoggerFactory.getLogger(getClass());
		accepters = new ArrayList<DataAccepter>();
		stopped = false;
		String filePath = this.getClass().getClassLoader().getResource("")
				.getPath()
				+ "../configuration.xml";
		port = CSNXMLParser.getSocketPort(filePath);
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			logger.info("Waiting for Sensor Connection from port {}", port);
			serverSocket = new ServerSocket(port);
			DataAccepter accepter;

			while (!isStopped()) {
				Socket socket = serverSocket.accept();
				logger.info("Sensor Client {} Connected(Accepted)",
						socket.getInetAddress());
				accepter = new DataAccepter(socket);
				accepter.start();
				accepters.add(accepter);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean isStopped() {
		return stopped;
	}

	public synchronized void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	class DataAccepter extends Thread implements MqttCallback {
		private static final String BROKER_URL = "tcp://localhost:1883";
		private static final String MQTT_CLIENT_ID = "Accepter-Node";
		private static final String MQTT_PUBS_TOPIC = "CSN/CENTRAL/DATA";
		private static final boolean MQTT_CLEAN_SESSION_OPT = true;
		private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
		private static final int MQTT_QOS_OPT = 1;
		private Logger logger;
		private MqttClient myClient;
		private MqttTopic topic;
		private MqttConnectOptions connOpt;
		private Socket socket;
		private BufferedReader br;
		private ObjectMapper jsonMapper;

		public DataAccepter(Socket socket) {
			super();
			logger = LoggerFactory.getLogger(getClass());
			this.socket = socket;
			try {
				InputStream is = socket.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void connBroker() {
			connOpt = new MqttConnectOptions();
			connOpt.setCleanSession(MQTT_CLEAN_SESSION_OPT);
			connOpt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_OPT);
			try {
				myClient = new MqttClient(BROKER_URL, MQTT_CLIENT_ID);
				myClient.setCallback(this);
				myClient.connect(connOpt);
			} catch (MqttException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		public void connectionLost(Throwable throwable) {
		}

		public void messageArrived(String s, MqttMessage mqttmessage)
				throws Exception {
		}

		public void deliveryComplete(IMqttDeliveryToken imqttdeliverytoken) {
		}

		public void run() {
			connBroker();
			try {
				String str = br.readLine();
				Strings.emptyToNull(str);
				logger.info("Sensor Data Coming");
				logger.info("Data: {}", str);

				topic = myClient.getTopic(MQTT_PUBS_TOPIC);
				jsonMapper = new ObjectMapper();
				if (str != null) {
					List<SensorData> sensorDataList;
					sensorDataList = jsonMapper.readValue(
							str,
							jsonMapper.getTypeFactory()
									.constructCollectionType(List.class,
											SensorData.class));
					for (SensorData sensorData : sensorDataList) {
						Set<String> topicPaths = NetworkMappingMap
								.getMappingMap().get(sensorData.getId());
						for (String topicPath : topicPaths) {
							logger.info(
									"Current Sensor Data is a member of {}",
									topicPath);
							String mqttTopicName = topicPath.replace('.', '/');
							topic = myClient.getTopic(mqttTopicName);
							ObjectMapper jsonMapper = new ObjectMapper();
							String jsonStr = null;
							try {
								jsonStr = jsonMapper
										.writeValueAsString(sensorData);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
							logger.info((new StringBuilder())
									.append("RePublished MSG: ")
									.append(jsonStr).toString());
							int pubQoS = 0;
							MqttMessage message = new MqttMessage(
									jsonStr.getBytes());
							message.setQos(pubQoS);
							message.setRetained(false);
							MqttDeliveryToken token = null;
							try {
								token = topic.publish(message);
								token.waitForCompletion();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				} else {
					logger.info("Data null");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void after() {
			try {
				myClient.disconnect();
				logger.info("Close Connection");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void close() {
			try {
				br.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}