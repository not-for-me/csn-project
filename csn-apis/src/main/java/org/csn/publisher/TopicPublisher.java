package org.csn.publisher;

public interface TopicPublisher {
	public void setConnection(String domain, String appName, String topicName);
	
	public void connect();
	
	public void close();
	
	public void publish(String id, String timestamp, String value);
	
	public void publish(Object data);
}
