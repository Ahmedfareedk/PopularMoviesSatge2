package com.example.amedfareed.movieapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Conference;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.amedfareed.movieapp.BuildConfig;
import com.example.amedfareed.movieapp.MovieAdapter.MovieAdapter;
import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.model.MovieResponses;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.example.amedfareed.movieapp.rest.RetrofitBuilder;
import com.example.amedfareed.movieapp.rest.GetMoviesService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIES_API_KEY = "";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter adapter;
    private GetMoviesService service;
    private List<PopularMovie> moviesList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeViewItems();
                Toast.makeText(MainActivity.this, "Movies refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        initializeViewItems();
    }

    public void initializeViewItems() {
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movies...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        moviesList = new ArrayList<>();
        adapter = new MovieAdapter(MainActivity.this, moviesList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, calculateNumberOfColumns(this)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        checkMoviesSortOrder();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.most_popular_action:
                fetchMovies(getString(R.string.pref_most_popular));
                return true;

            case R.id.top_rated_action:
                fetchMovies(getString(R.string.pref_top_rated));
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        checkMoviesSortOrder();
    }
    public void checkMoviesSortOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String mostPopularPOrder = getString(R.string.pref_most_popular);
        String topRatedOrder = getString(R.string.pref_top_rated);
        String sortOrderKey = getString(R.string.pref_sort_order_key);
        String sortOrder = preferences.getString(sortOrderKey, mostPopularPOrder);

        if (sortOrder.equals(mostPopularPOrder)) {
            fetchMovies(mostPopularPOrder);
        } else {
            fetchMovies(topRatedOrder);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (moviesList.isEmpty()) {
            checkMoviesSortOrder();
        }
    }
    public static int calculateNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpScreenWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int numberOfColumns = (int) (dpScreenWidth / scalingFactor);
        if (numberOfColumns < 2)
            numberOfColumns = 2;
        return numberOfColumns;
    }

    public void fetchMovies(String criteriaOrder){
       final RetrofitBuilder clientBuilder = new RetrofitBuilder();
        service = clientBuilder.createRetrofitBuilder()
                .create(GetMoviesService.class);
        Call<MovieResponses> moviesCall = null;
        if(criteriaOrder.equals(getString(R.string.pref_most_popular))){
            moviesCall = service.getPopularMovies(MOVIES_API_KEY);
        }else if(criteriaOrder.equals(getString(R.string.pref_top_rated))){
            moviesCall = service.getTopRatedMovies(MOVIES_API_KEY);
        }

        moviesCall.enqueue(new Callback<MovieResponses>() {
            @Override
            public void onResponse(Call<MovieResponses> call, Response<MovieResponses> response) {
                List<PopularMovie> movieList = response.body().getResults();
                recyclerView.setAdapter (new MovieAdapter(MainActivity.this, movieList));
                recyclerView.smoothScrollToPosition(0);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<MovieResponses> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fetching process failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

