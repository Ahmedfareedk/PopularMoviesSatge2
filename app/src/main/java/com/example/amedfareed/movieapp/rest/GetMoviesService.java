package com.example.amedfareed.movieapp.rest;

import com.example.amedfareed.movieapp.model.MovieResponses;
import com.example.amedfareed.movieapp.model.MovieReviewsResponses;
import com.example.amedfareed.movieapp.model.MovieTrailerReponse;
import com.example.amedfareed.movieapp.model.PopularMovie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by amedfareed on 06/03/18.
 */

public interface GetMoviesService {
    @GET("movie/{sort_by}")
    Call<MovieResponses> getMovies(@Path("sort_by") String sortOrder, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/videos")
    Call<MovieTrailerReponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/reviews")
    Call<MovieReviewsResponses> getMovieReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);
}
