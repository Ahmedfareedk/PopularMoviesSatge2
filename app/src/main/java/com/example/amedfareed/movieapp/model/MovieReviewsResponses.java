package com.example.amedfareed.movieapp.model;

import android.graphics.Movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MovieReviewsResponses {
    @SerializedName("id")
    private int reviewKey;
    @SerializedName("results")
    private List<MovieReviews> reviewResults;

    public int getReviewKey() {
        return reviewKey;
    }

    public void setReviewKey(int reviewKey) {
        this.reviewKey = reviewKey;
    }

    public List<MovieReviews> getReviewResults() {
        return reviewResults;
    }

    public void setReviewResults(List<MovieReviews> reviewResults) {
        this.reviewResults = reviewResults;
    }
}
