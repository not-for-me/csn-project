package org.csn.db;

import java.util.HashMap;
import java.util.Map;

import org.csn.util.CSNXMLParser;

public class DBConfiguration {
	public static final Map<String, String> DB_CONF_MAP = new HashMap<String, String>();

	public DBConfiguration() {
		String filePath = this.getClass().getClassLoader().getResource("")
				.getPath()
				+ "../configuration.xml";
		DB_CONF_MAP.putAll(CSNXMLParser.getPersitentDBConnInfo(filePath));
		DB_CONF_MAP.putAll(CSNXMLParser.getDBConnInfo(filePath));
	}
}
