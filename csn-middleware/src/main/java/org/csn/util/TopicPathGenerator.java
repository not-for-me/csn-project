package org.csn.util;

import org.csn.data.NetworkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicPathGenerator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String CSN_PREFIX = "CSN";
    private static final String CENTRAL_MODE_TOPIC_PATH = "CSN.CENTRAL.DATA";
    private static final String MULTI_NETWORK = "MULTI";
    private static final String SINGLE_NETWORK = "SINGLE";

    public static String getCentralizedTopicPath() {
        return CENTRAL_MODE_TOPIC_PATH;
    }

    public static String getNetworkTopicPath(String id, String name, NetworkType mode) {
        switch (mode) {
            case Single:
                return CSN_PREFIX + "." + SINGLE_NETWORK + "." + id + "." + name;
            case Multi:
                return CSN_PREFIX + "." + MULTI_NETWORK + "." + id + "." + name;
            default:
                return null;
        }
    }
}