package org.csn.client.impl;

import org.csn.client.CSNAPI;
import org.csn.common.HTTPRequestMethod;

public class CSNAPIImpl implements CSNAPI {
    private HTTPRequestMethod httpRequestMethod;

    @Override
    public String setCSNConfiguration(String json) {
//        httpRequestMethod.
        return null;
    }

    @Override
    public String getCSNConfiguration() {
        return null;
    }

    @Override
    public String deleteCSNConfiguration() {
        return null;
    }

    @Override
    public String startSystem(String json) {
        return null;
    }

    @Override
    public String restartSystem(String json) {
        return null;
    }

    @Override
    public String stopSystem(String json) {
        return null;
    }

    @Override
    public String getSystemStatus() {
        return null;
    }

    @Override
    public String getMessageBrokerStatus() {
        return null;
    }

    @Override
    public String getTopicInfoInMQ() {
        return null;
    }
}
