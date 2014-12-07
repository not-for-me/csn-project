package org.csn.util;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSNXMLParser {

    private static Element getXMLData(String filePath, String tag) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(filePath);

        Document document = (Document) builder.build(xmlFile);
        Element rootNode = document.getRootElement();
        Element dbNode = rootNode.getChild(tag);

        return dbNode;
    }

    public static Map<String, String> getDBConnInfo(String filePath) {
        Map<String, String> retMap = null;
        try {
            Element dbNode = getXMLData(filePath, "db");
            retMap = new HashMap<>();
            retMap.put("url", dbNode.getChildText("url"));
            retMap.put("user", dbNode.getChildText("user"));
            retMap.put("password", dbNode.getChildText("password"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retMap;
    }

    public static Map<String, String> getPersitentDBConnInfo(String filePath) {
        Map<String, String> retMap = null;
        try {
            Element dbNode = getXMLData(filePath, "persistentDB");
            retMap = new HashMap<>();
            retMap.put("url", dbNode.getChildText("url"));
            retMap.put("port", dbNode.getChildText("port"));
            retMap.put("dbName", dbNode.getChildText("dbName"));
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retMap;
    }

}