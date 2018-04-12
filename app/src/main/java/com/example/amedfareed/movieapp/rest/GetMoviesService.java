package com.example.amedfareed.movieapp.rest;

import com.example.amedfareed.movieapp.model.MovieResponses;
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
    @GET("movie/top_rated")
    Call<MovieResponses> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("movie/popular")
    Call<MovieResponses> getPopularMovies( @Query("api_key") String apiKey);
}
