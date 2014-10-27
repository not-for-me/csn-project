package org.csn.client;

public interface SensorNetworkAPI {
    public String postSensorNetworkMetadata(String input);
    public String getAllSensorNetworkMetadata();
    public String deleteAllSensorNetworkMetadata();

    public String getAllSensorNetworkIDList();
    public String getAllSensorNetworkTopicNameList();
    public String getAllSensorNetworkMembersList();

    public String getSensorNetworkMetadata(String id);
    public String deleteSensorNetworkMetadata(String id);
    public String getSensorNetworkMembers(String id);
    public String getSensorNetworkTopicName(String id);

    public String postSensorNetworkAllOptionalMetadata(String id, String input);
    public String getSensorNetworkAllOptionalMetadata(String id);
    public String updateSensorNetworkAllOptionalMetadata(String id, String input);
    public String deleteSensorNetworkAllOptionalMetadata(String id);

    public String getSensorNetworkOptionalMetadata(String id, String optName);
    public String updateSensorNetworkOptionalMetadata(String id, String optName, String optVal);
    public String deleteSensorNetworkOptionalMetadata(String id, String optName);

    public String postSensorNetworkAllTagMetadata(String id, String input);
    public String getSensorNetworkAllTagMetadata(String id);
    public String deleteSensorNetworkAllTagMetadata(String id);

    public String updateSensorNetworkTagMetadata(String id, String tag, String newTag);
    public String deleteSensorNetworkTagMetadata(String id, String tag);
}
