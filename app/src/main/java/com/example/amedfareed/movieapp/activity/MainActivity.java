package com.example.amedfareed.movieapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.amedfareed.movieapp.MovieAdapter.MovieAdapter;
import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.data.DbHelper;
import com.example.amedfareed.movieapp.model.MovieResponses;
import com.example.amedfareed.movieapp.model.MovieTrailerReponse;
import com.example.amedfareed.movieapp.model.PopularMovie;
import com.example.amedfareed.movieapp.rest.RetrofitBuilder;
import com.example.amedfareed.movieapp.rest.GetMoviesService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MOVIES_API_KEY = "9340a47ece52c04bb89b417830b3f601";
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    public MovieAdapter adapter;
    private GetMoviesService service;
    public List<PopularMovie> moviesList;
    public DbHelper favoriteDbHelper;
    private ProgressDialog progressDialog;
    private Parcelable state = null;
    GridLayoutManager layoutManager;
    private Bundle recyclerViewStateBundle;


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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movies...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        moviesList = new ArrayList<>();
        adapter = new MovieAdapter(MainActivity.this, moviesList);
        layoutManager = new GridLayoutManager(this, calculateNumberOfColumns(this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                fetchMovies(this.getString(R.string.pref_most_popular));
                return true;
            case R.id.top_rated_action:
                fetchMovies(this.getString(R.string.pref_top_rated));
                return true;
            case R.id.favorite_action:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(this, "preferences updated", Toast.LENGTH_LONG).show();
        checkMoviesSortOrder();
    }

    public void checkMoviesSortOrder() {
        SharedPreferences preferences = getSharedPreferences("movie_app", MODE_PRIVATE);
        String mostPopularOrder = getString(R.string.pref_most_popular);
        String topRatedOrder = getString(R.string.pref_top_rated);
        String favoriteMovies = getString(R.string.pref_favorites);
        String sortOrderKey = getString(R.string.pref_sort_order_key);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("most_popular", mostPopularOrder);
        editor.putString("top_rated", topRatedOrder);
        editor.apply();
        String sortOrder = preferences.getString(sortOrderKey, mostPopularOrder);
        if (sortOrder.equals(topRatedOrder)) {
            fetchMovies(topRatedOrder);
        } else if (sortOrder.equals(mostPopularOrder)) {
            fetchMovies(mostPopularOrder);
        } else if (sortOrder.equals(favoriteMovies)) {
            startActivity(new Intent(this, FavoritesActivity.class));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (state != null) {
                state = recyclerViewStateBundle.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                layoutManager.onRestoreInstanceState(state);
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (state != null) {
            state = layoutManager.onSaveInstanceState();
            recyclerViewStateBundle.putParcelable(BUNDLE_RECYCLER_LAYOUT, state);
        }
        Toast.makeText(this, "on Pause", Toast.LENGTH_SHORT).show();
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

    public void fetchMovies(String criteriaOrder) {
        final RetrofitBuilder clientBuilder = new RetrofitBuilder();
        service = clientBuilder.createRetrofitBuilder()
                .create(GetMoviesService.class);
        Call<MovieResponses> moviesCall = null;
        if (criteriaOrder.equals(getString(R.string.pref_most_popular))) {
            moviesCall = service.getPopularMovies(MOVIES_API_KEY);
        } else if (criteriaOrder.equals(getString(R.string.pref_top_rated))) {
            moviesCall = service.getTopRatedMovies(MOVIES_API_KEY);
        }
        moviesCall.enqueue(new Callback<MovieResponses>() {
            @Override
            public void onResponse(Call<MovieResponses> call, Response<MovieResponses> response) {
                List<PopularMovie> movieList = response.body().getResults();
                recyclerView.setAdapter(new MovieAdapter(MainActivity.this, movieList));
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