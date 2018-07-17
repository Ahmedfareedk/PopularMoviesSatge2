package com.example.amedfareed.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerReponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<MovieTrailer> results;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<MovieTrailer> getResults() {
        return results;
    }
    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }
}
