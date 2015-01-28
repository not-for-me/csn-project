package org.csn.component;

import java.util.Map;
import java.util.Set;

import org.csn.data.NetworkMetadata;
import org.csn.data.ReturnType;
import org.csn.data.SensorNetwork;

public interface SensorNetworkManager {
    public boolean isNetworkID(String id);

    /**
     * This method creates a network instance and insert it into the database.
     *
     * @param name    the name of the sensor network
     * @param members the set consisted of sensor networks as a member
     * @return If successfully done, it returns the id of network, or null.
     */
    public String registerNetwork(String name, Set<String> members, Set<NetworkMetadata> metadata, Set<String> tags);

    /**
     * This method returns all network instances.
     *
     * @return If successfully done, it returns all instances of networks, or null.
     */
    public Set<SensorNetwork> getAllNetworkResources();
    
    /**
     * This method returns all network instances.
     *
     * @return If successfully done, it returns all instances of networks, or null.
     */
    public Set<SensorNetwork> getNetworkResources(int index, int num);

    /**
     * This method deactivates all networks in the database;
     *
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType deactivateAllNetworks();

    /**
     * This method removes all networks in the database;
     *
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType removeAllNetworks();

    /**
     * This method returns all ids of networks in CSN.
     *
     * @return If successfully done, it returns the set of all network IDs, or null.
     */
    public Set<String> getAllNetworkIDs();

    /**
     * This method returns ids of networks in CSN.
     *
     * @param index the start location for getting networks
     * @param num   offset number
     * @return If successfully done, it returns the set of network IDs, or null.
     */
    public Set<String> getNetworkIDs(int index, int num);


    /**
     * This method returns all names of networks in CSN.
     *
     * @return If successfully done, it returns the set of all network names, or null.
     */
    public Set<String> getAllNetworkNames();

    /**
     * This method returns ids of networks in CSN.
     *
     * @param index the start location for getting networks
     * @param num   offset number
     * @return If successfully done, it returns the set of network IDs, or null.
     */
    public Set<String> getNetworkNames(int index, int num);


    /**
     * @return
     */
    public Set<String> getAllMemberNetworkIDs();

    public Map<String, Set<String>> getMappingMapInfo();

    /**
     * This method returns topic paths of all networks in CSN.
     *
     * @return If successfully done, it returns the set of topic paths of all networks, or null.
     */
    public Set<String> getAllNetworkTopicPaths();

    /**
     * This method returns topic paths of networks in CSN.
     *
     * @param index the start location for getting networks
     * @param num   offset number
     * @return If successfully done, it returns the set of topic paths of sensor networks, or null.
     */
    public Set<String> getNetworkTopicPaths(int index, int num);

    /**
     * This method returns the all networks and their member ids.
     *
     * @return If successfully done, it returns the map consisted of networks and their member IDs, or null.
     */
    public Map<String, Set<String>> getAllNetworkAndMemberIDs();

    public Map<String, Set<String>> getAllNetworkAndMemberNames();

    /**
     * This method is used when initializing network list for centralized mode
     *
     * @return Map object whose key is topic path and value is set of member network IDs.
     */
    public Map<String, Set<String>> getAllTopicPathAndMemberIDs();

    /**
     * This method returns the number of operating networks in CSN.
     *
     * @return If successfully done, it returns the integer number or 0.
     */
    public int getOperatingNetworkCount();

    /**
     * This method returns the number of stopped networks in CSN.
     *
     * @return If successfully done, it returns the integer number or 0.
     */
    public int getStoppedNetworkCount();

    /**
     * This method returns the number of faluted networks in CSN.
     *
     * @return If successfully done, it returns the integer number or 0.
     */
    public int getFaultedNetworkCount();

    /**
     * This method returns the number of all networks in CSN.
     *
     * @return If successfully done, it returns the integer number or 0.
     */
    public int getAllNetworkCount();

    /**
     * This method returns the network object
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the network ID or null.
     */
    public SensorNetwork getNetwork(String id);

    public String getNetworkName(String id);

    /**
     * This method remove a sensor network.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType removeNetwork(String id);

    /**
     * This method check if a sensor network is or not.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns true, or false..
     */
    public boolean isItNetwork(String id);

    /**
     * This method returns the set of sensors in a sensor network.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the set of networks which has a member this network, or null.
     */
    public Set<SensorNetwork> getParentNetworks(String id);

    /**
     * This method returns the set of sensors in a sensor network.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the set of network IDs which has a member this network, or null.
     */
    public Set<String> getParentNetworkIDs(String id);

    public Set<String> getParentNetworkNames(String id);

    /**
     * This method returns the set of topic paths of parent networks.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the set of topic paths of parent networks, or null.
     */
    public Set<String> getParentNetworkTopicPaths(String id);

    /**
     * It returns If the networks has members or not.
     *
     * @param id sensor network's ID
     * @return If it has members, it returns true, or false.
     */
    public boolean hasMembers(String id);

    /**
     * This method returns the set of member networks in a sensor network.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the set of member networks, or null.
     */
    public Set<SensorNetwork> getMembers(String id);

    /**
     * This method returns the set of member network IDs in a sensor network.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the set of member network IDs, or null.
     */
    public Set<String> getMemberIDs(String id);

    public Set<String> getMemberNames(String id);

    /**
     * This method returns the topic path of a sensor networks in CSN.
     *
     * @param id sensor network's ID
     * @return If successfully done, it returns the topic path of this network, or null.
     */
    public String getTopicPath(String id);


    /**
     * This method adds network's metadata into database.
     *
     * @param id    Network's identifier
     * @param key   metadata key name
     * @param value metadata value
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType addMetadata(String id, String key, String value);

    /**
     * This method adds network's metadata into database.
     *
     * @param id             Network's ID
     * @param input_metadata key-value metadata input
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType addMetadata(String id, Set<NetworkMetadata> input_metadata);

    /**
     * This method returns network's metadata map.
     *
     * @param id Network's ID
     * @return If successfully done, it returns map of metadata, or null
     */
    public Set<NetworkMetadata> getMetadata(String id);

    /**
     * This method returns the set of network's metadata keys.
     *
     * @param id Network's ID
     * @return If successfully done, it returns set of metadata keys, or null
     */
    public Set<String> getMetadataKeys(String id);

    /**
     * This method returns network's metadata value.
     *
     * @param id  Sensor Network's identifier
     * @param key metadata key name
     * @return If successfully done, it returns metadata value, or null
     */
    public String getMetadataValue(String id, String key);

    /**
     * This method update network's metadata in database.
     *
     * @param id        Network's ID
     * @param key       metadata key name
     * @param new_value New metadata value
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType updateMetadataValue(String id, String key, String new_value);

    /**
     * This method update network's metadata in database.
     *
     * @param id             Network's ID
     * @param input_metadata key-value metadata input
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType updateMetadata(String id, Map<String, String> input_metadata);

    /**
     * This method remove network's metadata in database.
     *
     * @param id Network's ID
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType removeMetadata(String id);

    /**
     * This method remove network's metadata in database.
     *
     * @param id  Network's ID
     * @param key metadata key name
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType removeMetadataValue(String id, String key);

    /**
     * This method check if whether the value of metadata is or not.
     *
     * @param id  Network's ID
     * @param key metadata key name
     * @return If successfully done, it returns true, or false..
     */
    public boolean isItMetadataValue(String id, String key);

    /**
     * This method add a new concept to a network.
     *
     * @param concept the concept value
     * @param id      Network's ID
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType addTag(String concept, String id);

    /**
     * This method add new concepts to a network.
     *
     * @param conceptSet the set of concept values
     * @param id         Network's ID
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType addTags(Set<String> conceptSet, String id);

    public ReturnType addTags(String concept, Set<String> idSet);

    public ReturnType createTag(String concept);

    public String getTagID(String concept);

    /**
     * It checks if the concept is already added on the network or nor.
     *
     * @param concept the concept value
     * @param id      Network's ID
     * @return If the concept is added on the networks, return true, or false.
     */
    public boolean hasTag(String concept, String id);

    public Set<String> getAllTags();

    /**
     * This method returns all concepts added to this network.
     *
     * @param id Network's ID
     * @return If successfully done, it returns set of concepts, or null.
     */
    public Set<String> getAllTags(String id);

    public ReturnType removeTag(String concept);


    /**
     * This method remove a concept of network.
     *
     * @param concept concept value which will be removed
     * @param id      Network's ID
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType removeTag(String concept, String id);

    /**
     * This method remove all concepts
     *
     * @param id Network's ID
     * @return If successfully done, it returns Done, or Error.
     */
    public ReturnType removeAllTags(String id);

    public Set<String> searchNetworkWithTag(String concept);
}
