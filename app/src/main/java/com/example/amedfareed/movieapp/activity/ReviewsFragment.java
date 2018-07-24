package com.example.amedfareed.movieapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amedfareed.movieapp.MovieAdapter.ReviewsAdapter;
import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.model.MovieReviews;
import com.example.amedfareed.movieapp.model.MovieReviewsResponses;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.example.amedfareed.movieapp.rest.GetMoviesService;
import com.example.amedfareed.movieapp.rest.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    @BindView(R.id.reviews_rv)
    RecyclerView reviewsRV;
    List<MovieReviews> reviewsList;
    ReviewsAdapter adapter;
    private static final String MOVIES_API_KEY = "9340a47ece52c04bb89b417830b3f601";
    public ReviewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void initViews(){
        reviewsList = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(), reviewsList);
        reviewsRV.setItemAnimator(new DefaultItemAnimator());
        reviewsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsRV.setAdapter(adapter);
        fetchMoviesReviews();
    }

    public void fetchMoviesReviews() {
        Intent intent = getActivity().getIntent();
        int id = intent.getExtras().getInt("id");
        try {
            RetrofitBuilder builder = new RetrofitBuilder();
            GetMoviesService service = builder.createRetrofitBuilder().create(GetMoviesService.class);
            Call<MovieReviewsResponses> call = service.getMovieReviews(id, MOVIES_API_KEY);
            call.enqueue(new Callback<MovieReviewsResponses>() {
                @Override
                public void onResponse(Call<MovieReviewsResponses> call, Response<MovieReviewsResponses> response) {
                    List<MovieReviews> list = response.body().getReviewResults();
                    reviewsRV.setAdapter(new ReviewsAdapter(getContext(), list));
                    reviewsRV.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<MovieReviewsResponses> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
