package com.example.amedfareed.movieapp.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;



import com.example.amedfareed.movieapp.MovieAdapter.TrailerAdapter;
import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.model.MovieTrailer;
import com.example.amedfareed.movieapp.model.MovieTrailerReponse;
import com.example.amedfareed.movieapp.rest.GetMoviesService;
import com.example.amedfareed.movieapp.rest.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Retrofit;

/**
 * Created by amedfareed on 25/04/18.
 */

public class TrailersFragment extends Fragment {
    @BindView(R.id.trailer_recyclerView)
    RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private List<MovieTrailer> trailerList;
    private GetMoviesService service;
    private static final String MOVIES_API_KEY = "9340a47ece52c04bb89b417830b3f601";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trailer, container, false);
        ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void initViews() {
        trailerList = new ArrayList<>();
        adapter = new TrailerAdapter(getContext(), trailerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        fetchMovieTrailers();
    }
    public void fetchMovieTrailers() {
        Intent intent = getActivity().getIntent();
        int id = intent.getExtras().getInt("id");
        try {
            RetrofitBuilder clientBuilder = new RetrofitBuilder();
            service = clientBuilder.createRetrofitBuilder().create(GetMoviesService.class);
            Call<MovieTrailerReponse> call = service.getMovieTrailer(id, MOVIES_API_KEY);
            call.enqueue(new Callback<MovieTrailerReponse>() {
                @Override
                public void onResponse(Call<MovieTrailerReponse> call, Response<MovieTrailerReponse> response) {
                    List<MovieTrailer> list = response.body().getResults();
                    recyclerView.setAdapter(new TrailerAdapter(getContext(), list));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<MovieTrailerReponse> call, Throwable t) {
                    Toast.makeText(getContext(), "failed to load trailers", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "failed to load trailers", Toast.LENGTH_SHORT).show();

        }
    }

}
