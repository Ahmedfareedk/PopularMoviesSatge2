package com.example.amedfareed.movieapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.amedfareed.movieapp.MovieAdapter.FavoriteMoviesAdapter;
import com.example.amedfareed.movieapp.MovieAdapter.MovieAdapter;
import com.example.amedfareed.movieapp.R;
import com.example.amedfareed.movieapp.data.MoviesContract;
import com.example.amedfareed.movieapp.model.PopularMovie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FavoriteMoviesAdapter adapter;
    private GridLayoutManager layoutManager;

    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    private Parcelable state;
    private static Bundle favRecState;

    @BindView(R.id.fav_recyclerView)
    RecyclerView favRecyclerView;
    List<PopularMovie> favList;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(0, null, FavoritesActivity.this);
        layoutManager = new GridLayoutManager(this, MainActivity.calculateNumberOfColumns(this));
        favRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FavoriteMoviesAdapter(this, cursor);
        favRecyclerView.setHasFixedSize(true);
        favRecyclerView.setLayoutManager(layoutManager);
        favRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        favRecState = new Bundle();
        state = favRecyclerView.getLayoutManager().onSaveInstanceState();
        favRecState.putParcelable(BUNDLE_RECYCLER_LAYOUT, state);
    }
  @Override
    protected void onResume() {
        super.onResume();
        if(favRecState != null){
            state = favRecState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            layoutManager.onRestoreInstanceState(state);
        }
    }

   /* @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (favRecState != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    state = favRecState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
                    layoutManager.onRestoreInstanceState(state);
                }
            }, 50);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(MainActivity.calculateNumberOfColumns(FavoritesActivity.this));
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setSpanCount(MainActivity.calculateNumberOfColumns(FavoritesActivity.this));
        }
        favRecyclerView.setLayoutManager(layoutManager);
    }
*/
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, MoviesContract.MoviesDateBase.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            adapter = new FavoriteMoviesAdapter(FavoritesActivity.this, data);
            favRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "no favorite movies", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}
