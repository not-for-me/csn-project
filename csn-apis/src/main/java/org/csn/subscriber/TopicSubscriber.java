package org.csn.subscriber;

public interface TopicSubscriber {
    public void setConnection(String domain, String appName, String topicName);

    public void setMessageCallback(MessageCallback callback);

    public void subscribe();

    public void unsubscribe();
}
