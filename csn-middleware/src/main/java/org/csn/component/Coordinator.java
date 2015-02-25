package org.csn.component;

import java.util.Map;

import org.csn.component.impl.DataManagerImpl;
import org.csn.component.impl.MessageQueueManagerImpl;
import org.csn.component.impl.SensorNetworkManagerImpl;
import org.csn.data.CSNConfiguration;
import org.csn.data.NetworkMappingMap;
import org.csn.data.ReturnType;
import org.csn.util.LogPrinter;
import org.csn.util.TimeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages the life cycle of CSN system.
 * 
 * @author NFM
 */
public class Coordinator {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(Coordinator.class.getClass());

	private CSNConfiguration configuration;
	private SensorNetworkManager sensorNetworkMgr;
	private DataManager dataManager;
	private MessageQueueManager mqManager;
	private boolean working = false;

	public Coordinator(String csnName) {
		configuration = new CSNConfiguration(csnName);
		setCreationTime();
	}

	public Coordinator(CSNConfiguration configuration) {
		this.configuration = configuration;
		setCreationTime();
	}

	private void setCreationTime() {
		configuration.setCreationTime(TimeGenerator.getCurrentTimestamp());
	}

	/**
	 * This method initialize all components of CSN which are Data, Sensor
	 * Network, and Message Queue Manager.
	 *
	 * @return If successfully done, it returns Done, or Error.
	 */
	public ReturnType initCSN() {
		LOGGER.info("Initializing Modules ...");
		createSubModuleInstance();
		dataManager.initDataManager(configuration.isPersistOption());

		LOGGER.info("Importing Network Member mapping list from db ...");
		NetworkMappingMap.setMemberMappingMap(sensorNetworkMgr
				.getMappingMapInfo());
		LOGGER.info("Imported Network Member mapping list {}",
				NetworkMappingMap.getMappingMap().toString());
		return ReturnType.Done;
	}

	private void createSubModuleInstance() {
		setSensorNetworkManager(new SensorNetworkManagerImpl());
		setDataManager(new DataManagerImpl());
		setMessageQueueManager(new MessageQueueManagerImpl());
	}

	/**
	 * This method start the CSN system.
	 *
	 * @return If successfully done, it returns Done, or Error.
	 */
	public ReturnType startCSN() {
		LOGGER.info("Setting Data Deliverer Configuration ...");
		mqManager.setDataDelivererConfiguration("xbean:activemq.xml");

		LOGGER.info("Starting Data Deliverer ...");
		mqManager.startDataDeliverer();

		try {
			final int waitSec = 1;
			LOGGER.info(
					"Waiting {} secs for safely configuring Data Deliverer ...",
					waitSec);
			Thread.sleep(waitSec * 1000);
		} catch (InterruptedException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
			return ReturnType.Error;
		}

		LOGGER.info("Starting Data Manager ...");
		dataManager.startDataManager();
		working = true;
		return ReturnType.Done;
	}

	/**
	 * This method restart the CSN system.
	 *
	 * @return If successfully done, it returns Done, or Error.
	 */
	public ReturnType restartCSN() {
		terminateCSN();
		LOGGER.info("Restarting CSN system ...");
		startCSN();
		return ReturnType.Done;
	}

	/**
	 * This method terminates the CSN system.
	 *
	 * @return If successfully done, it returns Done, or Error.
	 */
	public ReturnType terminateCSN() {
		LOGGER.info("Terminating Data Manager ...");
		dataManager.stopDataManager();

		LOGGER.info("Terminating Data Deliverer ...");
		mqManager.stopDataDeliverer();
		try {
			final int waitSec = 1;
			LOGGER.info("Waiting {} secs...", waitSec);
			Thread.sleep(waitSec * 1000);
		} catch (InterruptedException e) {
			LogPrinter.printErrorLog(LOGGER, e.getClass().toString(),
					e.getMessage());
		}
		working = false;
		return ReturnType.Done;
	}

	/**
	 * This method checks the working status of CSN system.
	 *
	 * @return If successfully working, it returns true, or false.
	 */
	public boolean isWorking() {
		return working;
	}

	/**
	 * This method create a current snapshot of CSN system.
	 * 
	 * @return
	 */
	public ReturnType backupSystem() {
		// TODO This method is not implemented.
		return ReturnType.Done;
	}

	/**
	 * It returns the backup lists from database.
	 *
	 * @return The map object whose key is backup Date and value is id of backup
	 *         information.
	 */
	public Map<String, String> getBackupList() {
		return null;
	}

	/**
	 * @param id
	 * @return
	 */
	public ReturnType restoreSystem(String id) {
		return ReturnType.Done;
	}

	/**
	 * It returns the configuration information of the CSN system.
	 *
	 * @return configuration information of the CSN system
	 */
	public CSNConfiguration getCsnConfiguration() {
		return configuration;
	}

	/**
	 * It returns the SensorNetworkManager object.
	 *
	 * @return SensorNetworkManager object
	 */
	public SensorNetworkManager getSensorNetworkManager() {
		return sensorNetworkMgr;
	}

	/**
	 * It returns DataManager object
	 *
	 * @return DataManager object.
	 */
	public DataManager getDataManager() {
		return dataManager;
	}

	/**
	 * It returns MessageQueueManager
	 *
	 * @return MessageQueueManager object.
	 */
	public MessageQueueManager getMessageQueueManager() {
		return mqManager;
	}

	public void setSensorNetworkManager(
			SensorNetworkManager sensorNetworkManager) {
		this.sensorNetworkMgr = sensorNetworkManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void setMessageQueueManager(MessageQueueManager mqManager) {
		this.mqManager = mqManager;
	}
}