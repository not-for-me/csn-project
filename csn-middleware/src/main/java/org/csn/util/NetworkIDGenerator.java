package org.csn.util;

import org.csn.db.CSNDAOFactory;
import org.csn.db.dao.SensorNetworkDAO;

import java.util.UUID;

public class NetworkIDGenerator {
    public static String getUUID() {
        SensorNetworkDAO dao = new CSNDAOFactory().sensorNetworkDAO();
        String tempNewID;
        do {
            tempNewID = UUID.randomUUID().toString().substring(0,8);
        }while(dao.isNetworkID(tempNewID));
        return tempNewID;
    }
}
