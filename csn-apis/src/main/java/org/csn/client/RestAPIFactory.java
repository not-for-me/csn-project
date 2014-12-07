package org.csn.client;

import org.csn.client.impl.CSNAPIImpl;
import org.csn.client.impl.SensorNetworkAPIImpl;

public class RestAPIFactory {
    public CSNAPI csnAPI() {
        return new CSNAPIImpl();
    }

    public SensorNetworkAPI sensorNetworkAPI() {
        return new SensorNetworkAPIImpl();
    }

    public SensorNetworkAPI searchAPI() {
        return new SearchAPIImpl();
    }

}
