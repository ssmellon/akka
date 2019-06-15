package com.karl.pre.message;

import java.io.Serializable;

public class ClusterMessage implements Serializable
{
    private String id;

    public ClusterMessage() {
    }

    public ClusterMessage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
