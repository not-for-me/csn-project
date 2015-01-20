package org.csn.component.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.csn.component.DataManager;
import org.csn.component.threads.CentralizedDataDistributor;
import org.csn.component.threads.CentralizedDataSubscriber;
import org.csn.component.threads.DataPersistenceWorker;
import org.csn.component.threads.SensorDataReceiver;
import org.csn.data.ReturnType;
import org.csn.data.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataManagerImpl implements DataManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private SensorDataReceiver receiverThread;
	private CentralizedDataSubscriber subscriberThread;
	private CentralizedDataDistributor distributorThread;
	private DataPersistenceWorker persistenceThread;
	private BlockingQueue<SensorData> queue;
	private boolean persistOption;

	private static final String receiverThreadName = "Sensor Data Receiver";
	private static final String distributorThreadName = "Data Distributor";
	private static final String subThreadName = "Data Subscriber";
	private static final String persistenceThreadName = "Data Persistence Worker";

	public DataManagerImpl() {
		queue = new ArrayBlockingQueue<SensorData>(1024);
	}

	public boolean isPersistOption() {
		return persistOption;
	}

	public ReturnType initDataManager(boolean persistOption) {
		logger.info("Initializing Thread Instances");
		this.initSensorDataThread();
		this.initSubscriberThread();
		this.initPublisherThread();
		if (persistOption) {
			logger.info("Persistent Thread Initializing");
			this.persistOption = persistOption;
			this.initPersistenceThread();
		}
		return ReturnType.Done;
	}

	public ReturnType startDataManager() {
		logger.info("Starting Thread Instances");
		this.startReceiverThread();
		this.startSubscriberThread();
		this.startPublisherThread();
		if (this.persistOption)
			this.startPersistenceWorkerThread();
		return ReturnType.Done;
	}

	public ReturnType restartDataManager() {
		return ReturnType.Done;
	}

	public ReturnType stopDataManager() {
		logger.info("Stopping Thread Instances");
		this.stopReceiverThread();
		this.stopSubscriberThread();
		this.stopPublisherThread();
		if (this.persistOption)
			this.stopPersistenceWorkerThread();
		return ReturnType.Done;
	}
	
	public ReturnType initSensorDataThread() {
		logger.info("Create SensorDataThread Thread ...");
		receiverThread = new SensorDataReceiver(receiverThreadName);
		return ReturnType.Done;
	}

	public ReturnType initSubscriberThread() {
		logger.info("Create CentralizedDataSubscriber Thread ...");
		subscriberThread = new CentralizedDataSubscriber(subThreadName, queue);
		return ReturnType.Done;
	}

	public ReturnType initPublisherThread() {
		logger.info("Create CentralizedDataDistributor Thread ...");
		distributorThread = new CentralizedDataDistributor(
				distributorThreadName, queue);
		return ReturnType.Done;
	}

	public ReturnType initPersistenceThread() {
		logger.info("Create Persistence Thread ...");
		persistenceThread = new DataPersistenceWorker(persistenceThreadName);
		return ReturnType.Done;
	}
	
	public ReturnType startReceiverThread() {
		logger.info("Start Receiver Thread");
		receiverThread.start();
		return ReturnType.Done;
	}

	public ReturnType startSubscriberThread() {
		logger.info("Start Subscriber Thread");
		subscriberThread.start();
		return ReturnType.Done;
	}

	public ReturnType startPublisherThread() {
		logger.info("Start Publisher Thread");
		distributorThread.start();
		return ReturnType.Done;
	}

	public ReturnType startPersistenceWorkerThread() {
		logger.info("Start Persistence Thread");
		persistenceThread.start();
		return ReturnType.Done;
	}

	public ReturnType stopReceiverThread() {
		logger.info("Stop Receiver Data Manager");
		receiverThread.setStopped(true);
		return ReturnType.Done;
	}
	
	public ReturnType stopSubscriberThread() {
		logger.info("Stop Subscriber Data Manager");
		subscriberThread.setStopped(true);
		return ReturnType.Done;
	}

	public ReturnType stopPublisherThread() {
		logger.info("Stop Publisher Data Manager");
		distributorThread.setStopped(true);
		return ReturnType.Done;
	}

	public ReturnType stopPersistenceWorkerThread() {
		logger.info("Stop Historical Data Manager");
		persistenceThread.setStopped(true);
		return ReturnType.Done;
	}
}
