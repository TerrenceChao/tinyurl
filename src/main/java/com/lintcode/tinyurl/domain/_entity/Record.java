package com.lintcode.tinyurl.domain._entity;


public class Record {
    private Integer id;
    private String longUrl;
    private String tinyUrl;

    public Integer getId() {
        return id;
    }

    public Record setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public Record setLongUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public Record setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
        return this;
    }
}