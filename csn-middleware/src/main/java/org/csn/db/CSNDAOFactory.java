package org.csn.db;

import org.csn.db.dao.SearchDAO;
import org.csn.db.dao.TagDAO;
import org.csn.db.dao.SensorNetworkDAO;

public class CSNDAOFactory {

    public SensorNetworkDAO sensorNetworkDAO() {
        return new SensorNetworkDAO(connectionMaker());
    }

    public TagDAO semanticConceptDAO() {
        return new TagDAO(connectionMaker());
    }

    public SearchDAO searchDAO() {
        return new SearchDAO(connectionMaker());
    }

    private ConnectionMaker connectionMaker() {
        return new JDBCConnectionMaker();
    }
}
