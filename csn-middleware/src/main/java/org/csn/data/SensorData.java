package org.csn.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorData {
    private String ID;
    private String Timestamp;
    private String Value;

    public SensorData() {

    }

    public SensorData (String ID, String Timestamp, String Value) {
        this.ID = ID;
        this.Timestamp = Timestamp;
        this.Value = Value;
    }

    public String getID() {
        return ID;
    }

    @JsonProperty("ID")
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    @JsonProperty("Timestamp")
    public void setTimestamp(String timestamp) {
        this.Timestamp = timestamp;
    }

    public String getValue() {
        return Value;
    }

    @JsonProperty("Value")
    public void setValue(String value) {
        this.Value = value;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "ID='" + ID + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}