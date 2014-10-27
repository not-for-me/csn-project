package org.csn.data;

import java.util.Set;

public class NetworkSeed {
    private String name;
    Set<String> members;
    Set<String> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "NetworkSeed{" +
                "name='" + name + '\'' +
                ", members=" + members +
                ", tags=" + tags +
                '}';
    }
}
