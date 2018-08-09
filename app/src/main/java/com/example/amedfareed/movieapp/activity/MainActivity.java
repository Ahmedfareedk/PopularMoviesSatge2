package com.example.amedfareed.movieapp.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import com.example.amedfareed.movieapp.MovieAdapter.FavoriteMoviesAdapter;
import com.example.amedfareed.movieapp.MovieAdapter.MovieAdapter;
import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.data.DbHelper;
import com.example.amedfareed.movieapp.data.MoviesContract;
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
    private static final String MOVIE_APP_NAME = "movie_app";
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    public MovieAdapter adapter;
    private GetMoviesService service;
    public List<PopularMovie> moviesList;
    private ProgressDialog progressDialog;
    private Parcelable state = null;
    GridLayoutManager layoutManager;
    private static boolean isClicked = false;
    private static final int LOADER_ID = 22;
    private static Cursor cursor;
    private static Bundle recBundleState;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initializeViewItems();
                    }
                }, 100);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        initializeViewItems();
        Toast.makeText(this, "on Create", Toast.LENGTH_SHORT).show();
    }

    public void initializeViewItems() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movies...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        moviesList = new ArrayList<>();
        adapter = new MovieAdapter(MainActivity.this, moviesList);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, calculateNumberOfColumns(this));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
        SharedPreferences preferences = getSharedPreferences(MOVIE_APP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String sortOrderKey = getString(R.string.pref_sort_order_key);
        switch (id) {
            case R.id.most_popular_action:
                String mostPopularOrder = getString(R.string.pref_most_popular);
                editor.putString(sortOrderKey, mostPopularOrder);
                editor.apply();
                fetchMovies(mostPopularOrder);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Most Popular Movies");
                }
                return true;
            case R.id.top_rated_action:
                String topRatedOrder = getString(R.string.pref_top_rated);
                editor.putString(sortOrderKey, topRatedOrder);
                editor.apply();
                fetchMovies(topRatedOrder);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Top Rated Movies");
                }
                return true;
            case R.id.favorite_action:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            default:
                return false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        recBundleState = new Bundle();
        state = recyclerView.getLayoutManager().onSaveInstanceState();
        recBundleState.putParcelable(BUNDLE_RECYCLER_LAYOUT, state);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (recBundleState != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    state = recBundleState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                    layoutManager.onRestoreInstanceState(state);
                }
            }, 50);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setSpanCount(calculateNumberOfColumns(this));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(calculateNumberOfColumns(this));
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(this, "preferences updated", Toast.LENGTH_LONG).show();
        checkMoviesSortOrder();
    }

    public void checkMoviesSortOrder() {
        SharedPreferences preferences = getSharedPreferences(MOVIE_APP_NAME, MODE_PRIVATE);
        String mostPopularOrder = getString(R.string.pref_most_popular);
        String topRatedOrder = getString(R.string.pref_top_rated);
        String sortOrderKey = getString(R.string.pref_sort_order_key);
        String sortOrder = preferences.getString(sortOrderKey, mostPopularOrder);
        if (sortOrder.equals(topRatedOrder)) {
            fetchMovies(topRatedOrder);
        } else if (sortOrder.equals(mostPopularOrder)) {
            fetchMovies(mostPopularOrder);
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

    public void fetchMovies(String criteriaOrder) {
        final RetrofitBuilder clientBuilder = new RetrofitBuilder();
        service = clientBuilder.createRetrofitBuilder()
                .create(GetMoviesService.class);
        Call<MovieResponses> moviesCall = null;
        if (criteriaOrder.equals(getString(R.string.pref_most_popular))) {
            moviesCall = service.getMovies(getString(R.string.most_popular_order), getString(R.string.api_key));
        } else if (criteriaOrder.equals(getString(R.string.pref_top_rated))) {
            moviesCall = service.getMovies(getString(R.string.top_Rated_order), getString(R.string.api_key));
        }
        moviesCall.enqueue(new Callback<MovieResponses>() {
            @Override
            public void onResponse(Call<MovieResponses> call, Response<MovieResponses> response) {
                if (response.isSuccessful()) {
                    List<PopularMovie> movieList = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(MainActivity.this, movieList));
                    recyclerView.smoothScrollToPosition(0);
                }else{
                    Toast.makeText(MainActivity.this, "issue with response", Toast.LENGTH_LONG).show();
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



