package com.example.amedfareed.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class MovieTrailer {
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;

    public MovieTrailer(String mKey, String mName) {
        this.key = mKey;
        this.name = mName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
