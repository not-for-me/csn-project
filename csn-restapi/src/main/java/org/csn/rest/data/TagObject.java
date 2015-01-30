package org.csn.rest.data;

public class TagObject {
    String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagObject{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
