package org.csn.component;

import org.csn.data.CSNConfiguration;
import org.csn.data.ReturnType;

import java.util.Map;

public interface Coordinator {
    /**
     * This method initialize all components of CSN
     * which are Data, Sensor Network, and Message Queue Manager.
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType initCSN();

    /**
     * This method start the CSN system.
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType startCSN();

    /**
     * This method restart the CSN system.
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType restartCSN();

    /**
     * This method terminates the CSN system.
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType terminateCSN();

    /**
     * This method checks the working status of CSN system.
     * @return If successfully working, it returns true, or false.
     */
    public boolean isWorking();

    public ReturnType backupSystem();

    /**
     * It returns the backup lists from database.
     * @return The map object whose key is backup Date and value is id of back information.
     */
    public Map<String, String> getBackupList();

    /**
     *
     * @param id
     * @return
     */
    public ReturnType restoreSystem(String id);

    /**
     * It returns the configuration information of the CSN system.
     * @return configuration information of the CSN system
     */
    public CSNConfiguration getCsnConfiguration();

    /**
     * It returns the SensorNetworkManager object.
     * @return SensorNetworkManager object
     */
    public SensorNetworkManager getSensorNetworkManager();

    /**
     * It returns DataManager object
     * @return DataManager object.
     */
    public DataManager getDataManager();

    /**
     * It returns MessageQueueManager
     * @return MessageQueueManager object.
     */
    public MessageQueueManager getMessageQueueManager();


}
