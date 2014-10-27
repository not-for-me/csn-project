package org.csn.component;

import org.csn.data.ReturnType;

public interface DataManager {

    public boolean isPersistOption();

    public ReturnType initDataManager(boolean persistOption);

    public ReturnType startDataManager();

    public ReturnType restartDataManager();

    public ReturnType stopDataManager();

    public ReturnType initSubscriberThread();

    public ReturnType initPublisherThread();

    public ReturnType initPersistenceThread();

    public ReturnType startSubscriberThread();

    public ReturnType startPublisherThread();

    public ReturnType startPersistenceWorkerThread();

    public ReturnType stopSubscriberThread();

    public ReturnType stopPublisherThread();

    public ReturnType stopPersistenceWorkerThread();
}
