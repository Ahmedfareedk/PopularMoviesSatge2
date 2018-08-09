package com.example.amedfareed.movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.amedfareed.movieapp.MovieAdapter.CategoryAdapter;
import com.example.amedfareed.movieapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private String movieTitle, overView, releaseDate, movieUserRating, moviePosterPath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        moviePosterPath = intent.getExtras().getString("poster_path");
        Picasso.with(this)
                    .load(BASE_POSTER_URL + moviePosterPath)
                    .into(moviePoster);
        initializeCollapsingLayout();
    }

    private void initializeCollapsingLayout() {
        final String title = getIntent().getExtras().getString("original_title");
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                boolean isShow = false;
                int appBarScrollRange = -1;
                if (appBarScrollRange == -1) {
                    appBarScrollRange = appBarLayout.getTotalScrollRange();
                }
                if (appBarScrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    toolbar.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
    }

