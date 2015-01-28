package org.csn.component.impl;

import org.csn.component.SensorNetworkManager;
import org.csn.data.NetworkMappingMap;
import org.csn.data.NetworkMetadata;
import org.csn.data.NetworkType;
import org.csn.data.ReturnType;
import org.csn.data.SensorNetwork;
import org.csn.db.CSNDAOFactory;
import org.csn.db.dao.SearchDAO;
import org.csn.db.dao.TagDAO;
import org.csn.db.dao.SensorNetworkDAO;
import org.csn.util.TopicPathGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SensorNetworkManagerImpl implements SensorNetworkManager {
    Logger logger = LoggerFactory.getLogger(SensorNetworkManagerImpl.class);

    private SensorNetworkDAO sensorNetworkDAO;
    private TagDAO tagDAO;
    private SearchDAO searchDAO;

    public SensorNetworkManagerImpl() {
        sensorNetworkDAO = new CSNDAOFactory().sensorNetworkDAO();
        tagDAO = new CSNDAOFactory().semanticConceptDAO();
        searchDAO = new CSNDAOFactory().searchDAO();
    }

    @Override
    public boolean isNetworkID(String id) {
        return sensorNetworkDAO.isNetworkID(id);
    }

    @Override
    public String registerNetwork(String name, Set<String> members, Set<NetworkMetadata> metadata, Set<String> tags) {
        boolean isSingle = false;
        int memberNum = ( members == null ) ? 1 : members.size();
        String id = sensorNetworkDAO.registerNetwork(name, memberNum);
        
        sensorNetworkDAO.addMetadata(id, metadata);
        
        if(tags != null)
            tagDAO.addTag(tags, id);
        if( members == null ) {
            members = new HashSet<>();
            members.add(id);
            isSingle = true;
        }

        String topicPath = ( isSingle ) ? TopicPathGenerator.getNetworkTopicPath(id, name, NetworkType.Single)
                : TopicPathGenerator.getNetworkTopicPath(id, name, NetworkType.Multi);

        _registerNetwork(id, members, topicPath);
        // Create another Single Network for the multi networks
//        if(!isSingle){
//            topicPath = TopicPathGenerator.getNetworkTopicPath(id, name, NetworkType.Single);
//            members = new HashSet<>();
//            members.add(id);
//            _registerNetwork(id, members, topicPath);
//        }
        return id;
    }

    private void _registerNetwork(String id, Set<String> members, String topicPath) {
        sensorNetworkDAO.addMember(id, members);
        logger.info("Created Network ID: {} /  Topic Path: {}", id, topicPath);
        sensorNetworkDAO.updateTopicPath(id, topicPath);

        for(String memberID : members)
            NetworkMappingMap.registerTopicPathToNetwork(memberID, topicPath);

        logger.info("Current Mapping List {}", NetworkMappingMap.getMappingMap().toString());
    }

    @Override
    public Set<SensorNetwork> getAllNetworkResources() {
        return sensorNetworkDAO.getAllNetworks();
    }
    
    @Override
    public Set<SensorNetwork> getNetworkResources(int index, int num) {
        return sensorNetworkDAO.getNetworks(index, num);
    }

    @Override
    public ReturnType deactivateAllNetworks() {
        NetworkMappingMap.initialize();
        return sensorNetworkDAO.deactivateAllNetworks();
    }

    @Override
    public ReturnType removeAllNetworks() {
        NetworkMappingMap.initialize();
        return sensorNetworkDAO.removeAllNetworks();
    }

    @Override
    public Set<String> getAllNetworkIDs() {
        return sensorNetworkDAO.getAllNetworkIDs();
    }


    @Override
    public Set<String> getNetworkIDs(int index, int num) {
        return sensorNetworkDAO.getNetworkIDs(index, num);
    }

    @Override
    public Set<String> getAllNetworkNames() {
        return sensorNetworkDAO.getAllNetworkNames();
    }

    @Override
    public Set<String> getNetworkNames(int index, int num) {
        return sensorNetworkDAO.getNetworkNames(index, num);
    }


    @Override
    public Set<String> getAllMemberNetworkIDs() {
        return sensorNetworkDAO.getAllMemberNetworkIDs();
    }

    @Override
    public Map<String, Set<String>> getMappingMapInfo() {
        Map<String, Set<String>> mappingMap = new HashMap<>();
        Set<String> memberNetworkIDs = this.getAllMemberNetworkIDs();

        for(String memberID : memberNetworkIDs) {
            Set<String> parentTopicPaths = this.getParentNetworkTopicPaths(memberID);
            mappingMap.put(memberID, parentTopicPaths);
        }
        return mappingMap;
    }

    @Override
    public Set<String> getAllNetworkTopicPaths() {
        return sensorNetworkDAO.getAllNetworkTopicPaths();
    }

    @Override
    public Set<String> getNetworkTopicPaths(int index, int num) {
        return sensorNetworkDAO.getNetworkTopicPaths(index, num);
    }

    @Override
    public Map<String, Set<String>> getAllNetworkAndMemberIDs() {
        return sensorNetworkDAO.getAllNetworkAndMemberIDs();
    }

    @Override
    public Map<String, Set<String>> getAllNetworkAndMemberNames() {
        return sensorNetworkDAO.getAllNetworkAndMemberNames();
    }

    @Override
    public Map<String, Set<String>> getAllTopicPathAndMemberIDs() {
        return sensorNetworkDAO.getAllTopicPathAndMemberIDs();
    }

    @Override
    public int getOperatingNetworkCount() {
        return sensorNetworkDAO.getOperatingNetworkCount();
    }

    @Override
    public int getStoppedNetworkCount() {
        return sensorNetworkDAO.getStoppedNetworkCount();
    }

    @Override
    public int getFaultedNetworkCount() {
        return sensorNetworkDAO.getFaultedNetworkCount();
    }

    @Override
    public int getAllNetworkCount() {
        return sensorNetworkDAO.getAllNetworkCount();
    }

    @Override
    public SensorNetwork getNetwork(String id) {
        return sensorNetworkDAO.getNetwork(id);
    }

    @Override
    public String getNetworkName(String id) {
        return sensorNetworkDAO.getNetworkName(id);
    }

    @Override
    public ReturnType removeNetwork(String id) {
        NetworkMappingMap.removeTopicPathFromNetwork(id, sensorNetworkDAO.getTopicPath(id));
        return sensorNetworkDAO.removeNetwork(id);
    }

    @Override
    public boolean isItNetwork(String id) {
        return sensorNetworkDAO.isItNetwork(id);
    }

    @Override
    public Set<SensorNetwork> getParentNetworks(String id) {
        return sensorNetworkDAO.getParentNetworks(id);
    }

    @Override
    public Set<String> getParentNetworkIDs(String id) {
        return sensorNetworkDAO.getParentNetworkIDs(id);
    }

    @Override
    public Set<String> getParentNetworkNames(String id) {
        return sensorNetworkDAO.getParentNetworkNames(id);
    }

    @Override
    public Set<String> getParentNetworkTopicPaths(String id) {
        return sensorNetworkDAO.getParentNetworkTopicPaths(id);
    }

    @Override
    public boolean hasMembers(String id) {
        return sensorNetworkDAO.hasMembers(id);
    }

    @Override
    public Set<SensorNetwork> getMembers(String id) {
        return sensorNetworkDAO.getMembers(id);
    }

    @Override
    public Set<String> getMemberIDs(String id) {
        return sensorNetworkDAO.getMemberIDs(id);
    }

    @Override
    public Set<String> getMemberNames(String id) {
        return sensorNetworkDAO.getMemberNames(id);
    }

    @Override
    public String getTopicPath(String id) {
        return sensorNetworkDAO.getTopicPath(id);
    }



    @Override
    public ReturnType addMetadata(String id, String key, String value) {
        return sensorNetworkDAO.addMetadata(id, key, value);
    }

    @Override
    public ReturnType addMetadata(String id, Set<NetworkMetadata> input_metadata) {
        return sensorNetworkDAO.addMetadata(id, input_metadata);
    }

    @Override
    public Set<NetworkMetadata> getMetadata(String id) {
        return sensorNetworkDAO.getMetadata(id);
    }

    @Override
    public Set<String> getMetadataKeys(String id) {
        return null;
    }

    @Override
    public String getMetadataValue(String id, String key) {
        return null;
    }

    @Override
    public ReturnType updateMetadataValue(String id, String key, String new_value) {
        return null;
    }

    @Override
    public ReturnType updateMetadata(String id, Map<String, String> input_metadata) {
        return null;
    }

    @Override
    public ReturnType removeMetadata(String id) {
        return null;
    }

    @Override
    public ReturnType removeMetadataValue(String id, String key) {
        return null;
    }

    @Override
    public boolean isItMetadataValue(String id, String key) {
        return false;
    }



    @Override
    public ReturnType addTag(String tag, String id) {
        return tagDAO.addTag(tag, id);
    }

    @Override
    public ReturnType addTags(Set<String> tagSet, String id) {
        return tagDAO.addTag(tagSet, id);
    }

    @Override
    public ReturnType addTags(String tag, Set<String> idSet) {
        return tagDAO.addTag(tag, idSet);
    }

    @Override
    public ReturnType createTag(String tag) {
        return tagDAO.createTag(tag);
    }

    @Override
    public String getTagID(String tag) {
        return tagDAO.getTagID(tag);
    }

    @Override
    public boolean hasTag(String tag, String id) {
        return tagDAO.hasTag(tag, id);
    }

    @Override
    public Set<String> getAllTags() {
        return tagDAO.getAllTags();
    }

    @Override
    public Set<String> getAllTags(String id) {
        return tagDAO.getAllTags(id);
    }

    @Override
    public ReturnType removeTag(String tag) {
        return tagDAO.removeTag(tag);
    }

    @Override
    public ReturnType removeTag(String tag, String id) {
        return tagDAO.removeTag(tag, id);
    }

    @Override
    public ReturnType removeAllTags(String id) {
        return tagDAO.removeAllTags(id);
    }

    @Override
    public Set<String> searchNetworkWithTag(String concept) {
        return searchDAO.searchNetworkWithTag(concept);
    }
}
