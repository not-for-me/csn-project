package org.csn.db;

import java.util.HashMap;
import java.util.Map;

import org.csn.util.CSNXMLParser;

public class DBConfiguration {
	private static Map<String, String> confMap = new HashMap<String, String>();

	public DBConfiguration() {
		String filePath = this.getClass().getClassLoader().getResource("")
				.getPath()
				+ "../configuration.xml";
		confMap.putAll(CSNXMLParser.getPersitentDBConnInfo(filePath));
		confMap.putAll(CSNXMLParser.getDBConnInfo(filePath));
	}

	public static Map<String, String> getDBConfMap() {
		return confMap;
	}
}
