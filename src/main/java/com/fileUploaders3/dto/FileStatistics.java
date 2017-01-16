package com.fileUploaders3.dto;

/**
 * Created by Vladimir on 14.01.2017.
 */
public class FileStatistics {
    private String key;
    private long size;
    private String eTag;

    public FileStatistics() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }
}
