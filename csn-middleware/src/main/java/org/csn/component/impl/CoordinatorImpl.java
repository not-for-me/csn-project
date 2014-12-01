package org.csn.component.impl;

import org.csn.component.Coordinator;
import org.csn.component.DataManager;
import org.csn.component.MessageQueueManager;
import org.csn.component.SensorNetworkManager;
import org.csn.data.CSNConfiguration;
import org.csn.data.NetworkMappingMap;
import org.csn.data.ReturnType;
import org.csn.util.TimeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class CoordinatorImpl implements Coordinator {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private CSNConfiguration configuration;
    private SensorNetworkManager sensorNetworkManager;
    private DataManager dataManager;
    private MessageQueueManager mqManager;
    private boolean working = false;

    public CoordinatorImpl(String name){
        configuration = new CSNConfiguration(name);
    }

    public CoordinatorImpl(String name, CSNConfiguration configuration){
        this.configuration = configuration;
        this.configuration.setCsnName(name);
        this.configuration.setCreationTime(TimeGenerator.getCurrentTimestamp());
    }

    @Override
    public ReturnType initCSN() {
        logger.info("Initializing Modules ...");
        createSubModuleInstance();
        dataManager.initDataManager(this.configuration.isPersistOption());

        logger.info("Importing Network Member mapping list from db ...");
        NetworkMappingMap.setMemberMappingMap(sensorNetworkManager.getMappingMapInfo());
        return ReturnType.Done;
    }

    private void createSubModuleInstance() {
        this.setSensorNetworkManager(new SensorNetworkManagerImpl());
        this.setDataManager(new DataManagerImpl());
        this.setMessageQueueManager(new MessageQueueManagerImpl());
    }

    @Override
    public ReturnType startCSN() {
        logger.info("Setting Data Deliverer Configuration ...");
        mqManager.setDataDelivererConfiguration("xbean:activemq.xml");

        logger.info("Starting Data Deliverer ...");
        mqManager.startDataDeliverer();

        try {
            int waitSec = 1;
            logger.info("Waiting {} secs for safely configuring Data Deliverer ...", waitSec);
            Thread.sleep( waitSec * 1000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ReturnType.Error;
        }

        logger.info("Starting Data Manager ...");
        dataManager.startDataManager();
        working = true;
        return ReturnType.Done;
    }

    @Override
    public ReturnType restartCSN() {
        terminateCSN();
        logger.info("Restarting CSN system ...");
        startCSN();
        return ReturnType.Done;
    }

    @Override
    public ReturnType terminateCSN() {
        logger.info("Terminating Data Manager ...");
        dataManager.stopDataManager();

        logger.info("Terminating Data Deliverer ...");
        mqManager.stopDataDeliverer();
        try {
            int waitSec = 1;
            logger.info("Waiting {} secs...", waitSec);
            Thread.sleep( waitSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        working = false;
        return ReturnType.Done;
    }

    @Override
    public boolean isWorking() {
        return working;
    }

    @Override
    public ReturnType backupSystem() {
        return ReturnType.Done;
    }

    @Override
    public Map<String, String> getBackupList() {
        return null;
    }

    @Override
    public ReturnType restoreSystem(String id) {
        return ReturnType.Done;
    }

    @Override
    public CSNConfiguration getCsnConfiguration() {
        return configuration;
    }

    @Override
    public SensorNetworkManager getSensorNetworkManager() {
        return sensorNetworkManager;
    }

    @Override
    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public MessageQueueManager getMessageQueueManager() {
        return mqManager;
    }

    public void setCsnConfiguration(CSNConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setSensorNetworkManager(SensorNetworkManager sensorNetworkManager) {
        this.sensorNetworkManager = sensorNetworkManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void setMessageQueueManager(MessageQueueManager mqManager) {
        this.mqManager = mqManager;
    }
}
