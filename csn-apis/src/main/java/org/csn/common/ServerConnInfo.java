package org.csn.common;

public class ServerConnInfo {
    private String defaultPath = "http://localhost:8080/csn-restful";
    public static final String CSN_URL = "/csn";
    public static final String SN_URL = "/networks";
    public static final String CONCEPT_URL = "/concepts";

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }
}
