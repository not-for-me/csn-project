package org.csn.component;

import org.csn.data.MessageQueue;
import org.csn.data.ReturnType;

import java.util.Map;
import java.util.Set;

public interface MessageQueueManager {

    public void createTopic(String topicPath);

    public void removeTopic(String topicPath);

    public long getMSGEnqueuedCNT(String topicPath);

    public long getMSGDequeuedCNT(String topicPath);

    public long getPublisherCount(String topicPath);

    public long getSubscriberCount(String topicPath);


    public ReturnType setDataDelivererConfiguration(String settings);

    public ReturnType startDataDeliverer();

    public ReturnType stopDataDeliverer();

    public long getTotalMSGEnqueueCount();

    public long getTotalMSGDequeueCount();

    public long getTotalConsumerCount();

    public long getTotalProducerCount();

    public Set<String> getTopicSubscribers();

    public int getStoreUsagePercentage();

    public int getMemoryUsagePercentage();

    public ReturnType doGarbageCollect();

    public ReturnType createSnapshotDeliverer();


    public Set<Map<String, Object>> getAllTopicStatus();

    public Map<String, Long> getAllTopicConsumerCount();

    public Map<String, Set<String>> getAllTopicSubscribers();

    public String getCurrentHealthStatus();
}
