package com.example.amedfareed.movieapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amedfareed on 09/06/18.
 */

public class MovieReviews {
    @SerializedName("id")
    private String reviewKey;
    @SerializedName("author")
    private String authorName;
    @SerializedName("content")
    private String reviewContent;

    public MovieReviews(String authorName, String reviewContent) {
        this.authorName = authorName;
        this.reviewContent = reviewContent;
    }

    public String getReviewKey(){
        return reviewKey;
    }
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
