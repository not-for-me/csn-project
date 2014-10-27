package org.csn.client;

import org.csn.api.csn.CSNAPIImpl;
import org.csn.api.network.SensorNetworkAPIImpl;
import org.csn.api.sensor.SensorAPIImpl;

public class RestAPIFactory {
    public CSNAPI csnAPI() {
        return new CSNAPIImpl();
    }

    public SensorAPI sensorAPI() {
        return new SensorAPIImpl();
    }

    public SensorNetworkAPI sensorNetworkAPI() {
        return new SensorNetworkAPIImpl();
    }
}
