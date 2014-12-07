package org.csn.db.dao;

import org.csn.data.SensorData;
import org.csn.db.CSNDAOFactory;
import org.csn.db.ConnectionMaker;
import org.csn.db.MongoDBConnectionMaker;
import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.*;

public class SearchDAO {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private TagDAO tagDAO;
    private DB mongoDB;
    private DBCollection sensorDataTable;
    private ConnectionMaker connectionMaker;

    public SearchDAO(ConnectionMaker connectionMaker){
        this.connectionMaker = connectionMaker;
        this.tagDAO = new CSNDAOFactory().semanticConceptDAO();
        this.mongoDB = new MongoDBConnectionMaker().getMongoDB();
        this.sensorDataTable = mongoDB.getCollection("SensorData");
    }

    public Set<String> searchNetworkWithTag(String tag) {
        Set<String> idSet = new HashSet<String>();
        try {
            Connection c = connectionMaker.makeConnection();
            PreparedStatement ps = c.prepareStatement(
                    "SELECT sn_id FROM csn_annotate WHERE tag_id = ?");
            String tag_id = tagDAO.getTagID(tag);
            ps.setString(1, tag_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                idSet.add(rs.getString(1));

            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            idSet = null;
        }
        return idSet;
    }

    public Set<String> searchNetworkWithConcepts(Set<String> tags) {
        Set<String> idSet = new HashSet<String>();
        try {
            Connection c = connectionMaker.makeConnection();

            String query_partial = "SELECT sn_id FROM csn_annotate WHERE tag_id IN (";
            int i = 0;
            Iterator<String> iter = tags.iterator();
            while( i < tags.size() - 1 ) {
                String tag_id = tagDAO.getTagID(iter.next());
                query_partial = query_partial + tag_id + ", ";
                i++;
            }
            String tag_id = tagDAO.getTagID(iter.next());
            query_partial = query_partial + tag_id + ")";
            query_partial = query_partial + " GROUP BY sn_id HAVING COUNT(tag_id) = " + tags.size();

            System.out.println(query_partial);
            PreparedStatement ps = c.prepareStatement(query_partial);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                idSet.add(rs.getString(1));

            ps.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            idSet = null;
        }
        return idSet;
    }

    public List<SensorData> searchSensorData(String startDate, String endDate) {
        List<SensorData> dataList = new ArrayList<SensorData>();
        DBCursor cursor;
        BasicDBObject query;
        BasicDBObject period = new BasicDBObject("$gt", startDate).append("$lt", endDate);
        query = new BasicDBObject("time", period);
        cursor = sensorDataTable.find(query, new BasicDBObject("_id",0));
        try {
            while(cursor.hasNext()) {
                DBObject jsonData = cursor.next();
                SensorData data = new SensorData(
                        (String) jsonData.get("id"),
                        (String) jsonData.get("time"),
                        (String) jsonData.get("val")
                );
                dataList.add(data);
            }
        } finally {
            cursor.close();
            return dataList;
        }
    }

    public List<SensorData> searchSensorData(String startDate, String unit, int duration) {
        String endDate = null;
        try {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            java.util.Date date = format.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            switch (unit) {
                case "day":
                    calendar.add(Calendar.DATE, duration);
                    break;
                case "hour":
                    calendar.add(Calendar.HOUR, duration);
                    break;
                case "min":
                    calendar.add(Calendar.MINUTE, duration);
                    break;
                case "sec":
                default:
                    calendar.add(Calendar.SECOND, duration);
                    break;
            }
            endDate = format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return searchSensorData(startDate, endDate);
    }

    public List<SensorData> searchSensorData(int num) {
        List<SensorData> dataList = new ArrayList<SensorData>();
        DBCursor cursor;
        cursor = sensorDataTable.find().sort(new BasicDBObject("time",-1)).limit(num);
        try {
            while(cursor.hasNext()) {
                DBObject jsonData = cursor.next();
                SensorData data = new SensorData(
                        (String) jsonData.get("id"),
                        (String) jsonData.get("time"),
                        (String) jsonData.get("val")
                );
                dataList.add(data);
            }
        } finally {
            cursor.close();
            return dataList;
        }
    }
}
