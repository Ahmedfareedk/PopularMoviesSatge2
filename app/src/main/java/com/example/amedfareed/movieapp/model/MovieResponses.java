package com.example.amedfareed.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amedfareed on 11/03/18.
 */

public class MovieResponses {
     @SerializedName("page")
    private int page;
     @SerializedName("results")
    private List<PopularMovie> results;
     @SerializedName("totalResults")
    private int totalResults;
     @SerializedName("totalPages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<PopularMovie> getResults() {
        return results;
    }

    public void setResults(List<PopularMovie> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
