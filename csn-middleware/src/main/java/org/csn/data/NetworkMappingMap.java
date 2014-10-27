package org.csn.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NetworkMappingMap {
    private static Map<String, Set<String>> mappingMap = new HashMap<String, Set<String>>();
    private static boolean updating = false;

    public static void registerTopicPathToNetwork(String id, String topicPath) {
        Set<String> topicPathSet = new HashSet<>();
        if(mappingMap.containsKey(id))
            topicPathSet = mappingMap.get(id);

        topicPathSet.add(topicPath);
        mappingMap.put(id,topicPathSet);
    }

    public static void removeTopicPathFromNetwork(String id, String topicPath) {
        Set<String> topicPathSet = mappingMap.get(id);
        topicPathSet.remove(topicPath);
    }

    public static void initialize() {
        mappingMap  = new HashMap<String, Set<String>>();
    }

    public static void setMemberMappingMap(Map<String, Set<String>> importedMap) {
        mappingMap = importedMap;
    }

    public static Map<String, Set<String>> getMappingMap() {
        return mappingMap;
    }
}
