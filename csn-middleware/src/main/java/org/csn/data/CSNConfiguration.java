package org.csn.data;

import org.csn.util.TimeGenerator;

public class CSNConfiguration {
    private String csnName;
    private String creationTime;
    private String adminName;
    private String adminEmail;
    private boolean persistOption;

    public CSNConfiguration() {
        this.csnName = "";
        this.creationTime = "";
        this.adminName = "";
        this.adminEmail = "";
        this.persistOption = true;
    }

    public CSNConfiguration(String csnName) {
        this.csnName = csnName;
        this.creationTime = TimeGenerator.getCurrentTimestamp();
        this.adminName = "";
        this.adminEmail = "";
        this.persistOption = true;
    }

    public CSNConfiguration(String csnName, String adminName, String adminEmail) {
        this.csnName = csnName;
        this.creationTime = TimeGenerator.getCurrentTimestamp();
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.persistOption = true;
    }

    public String getCsnName() {
        return csnName;
    }

    public void setCsnName(String csnName) {
        this.csnName = csnName;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isPersistOption() {
        return persistOption;
    }

    public void setPersistOption(boolean persistOption) {
        this.persistOption = persistOption;
    }

    @Override
    public String toString() {
        return "CSNConfiguration{" +
                "csnName='" + csnName + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", adminName='" + adminName + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", persistOption=" + persistOption +
                '}';
    }
}
