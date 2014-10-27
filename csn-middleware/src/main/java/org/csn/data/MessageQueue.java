package org.csn.data;

public class MessageQueue {
    private String networkID;
    private String topicPath;
    private String status;

    public String getNetworkID() {
        return networkID;
    }

    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    public String getTopicPath() {
        return topicPath;
    }

    public void setTopicPath(String topicPath) {
        this.topicPath = topicPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageQueue{" +
                "networkID='" + networkID + '\'' +
                ", topicPath='" + topicPath + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
