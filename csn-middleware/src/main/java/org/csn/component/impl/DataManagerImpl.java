package org.csn.component.impl;

import org.csn.component.DataManager;
import org.csn.component.threads.CentralizedDataDistributer;
import org.csn.component.threads.CentralizedDataSubscriber;
import org.csn.component.threads.DataPersistenceWorker;
import org.csn.data.ReturnType;
import org.csn.data.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataManagerImpl implements DataManager {
    Logger logger = LoggerFactory.getLogger(DataManagerImpl.class);

    private CentralizedDataSubscriber subsThread;
    private CentralizedDataDistributer pubThread;
    private DataPersistenceWorker persistenceThread;
    private BlockingQueue<SensorData> queue;
    private boolean persistOption;

    private static final String pubThreadName = "Data Publisher";
    private static final String subThreadName = "Data Subscriber";
    private static final String persitThreadName = "Data Persistence Worker";

    public DataManagerImpl() {
        queue = new ArrayBlockingQueue<SensorData>(1024);
    }

    public boolean isPersistOption() {
        return persistOption;
    }

    public ReturnType initDataManager(boolean persistOption) {
        logger.info("Initializing Thread Instances");
        this.initSubscriberThread();
        this.initPublisherThread();
        if(persistOption) {
            logger.info("Persistent Thread Initializing");
            this.persistOption = persistOption;
            this.initPersistenceThread();
        }
        return ReturnType.Done;
    }

    public ReturnType startDataManager() {
        logger.info("Starting Thread Instances");
        this.startSubscriberThread();
        this.startPublisherThread();
        if(this.persistOption)
            this.startPersistenceWorkerThread();
        return ReturnType.Done;
    }

    public ReturnType restartDataManager() {
        return ReturnType.Done;
    }

    public ReturnType stopDataManager() {
        logger.info("Stopping Thread Instances");
        this.stopSubscriberThread();
        this.stopPublisherThread();
        if(this.persistOption)
            this.stopPersistenceWorkerThread();
        return ReturnType.Done;
    }

    public ReturnType initSubscriberThread() {
        logger.info("Create CentralizedDataSubscriber Thread ...");
        subsThread = new CentralizedDataSubscriber(subThreadName, queue);
        return ReturnType.Done;
    }

    public ReturnType initPublisherThread() {
        logger.info("Create CentralizedDataDistributer Thread ...");
        pubThread = new CentralizedDataDistributer(pubThreadName, queue);
        return ReturnType.Done;
    }

    public ReturnType initPersistenceThread() {
        logger.info("Create Persistence Thread ...");
        persistenceThread = new DataPersistenceWorker(persitThreadName);
        return ReturnType.Done;
    }

    public ReturnType startSubscriberThread() {
        logger.info("Start Subscriber Thread");
        subsThread.start();
        return ReturnType.Done;
    }

    public ReturnType startPublisherThread() {
        logger.info("Start Publisher Thread");
        pubThread.start();
        return ReturnType.Done;
    }

    public ReturnType startPersistenceWorkerThread() {
        logger.info("Start Persistence Thread");
        persistenceThread.start();
        return ReturnType.Done;
    }

    public ReturnType stopSubscriberThread() {
        logger.info("Stop Subscriber Data Manager");
        subsThread.setStopped(true);
        return ReturnType.Done;
    }

    public ReturnType stopPublisherThread() {
        logger.info("Stop Publisher Data Manager");
        pubThread.setStopped(true);
        return ReturnType.Done;
    }

    public ReturnType stopPersistenceWorkerThread() {
        logger.info("Stop Historical Data Manager");
        persistenceThread.setStopped(true);
        return  ReturnType.Done;
    }
}
