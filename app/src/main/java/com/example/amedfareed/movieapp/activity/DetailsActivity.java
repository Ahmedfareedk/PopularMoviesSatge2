package com.example.amedfareed.movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.amedfareed.movieapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amedfareed on 26/03/18.
 */

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView movieName;
    @BindView(R.id.review)
    TextView movieReview;
    @BindView(R.id.user_rating_tv)
    TextView userRatingTV;
    @BindView(R.id.release_date_tv)
    TextView movieReleaseDate;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.collapse_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent.hasExtra("original_title")) {
            String moviePosterPath = intent.getExtras().getString("poster_path");
            String movieTitle = intent.getExtras().getString("original_title");
            String overView = intent.getExtras().getString("over_view");
            String releaseDate = intent.getExtras().getString("release_date");
            String movieUserRating = intent.getExtras().getString("user_rating");
            movieName.setText(movieTitle);
            movieReview.setText(overView);
            userRatingTV.setText(movieUserRating);
            movieReleaseDate.setText(releaseDate);
            Picasso.with(this)
                    .load(moviePosterPath)
                    .into(moviePoster);
        }
        initializeCollapsingLayout();
    }

    private void initializeCollapsingLayout() {
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
                    collapsingToolbarLayout.setTitle(movieName.getText());
                    toolbar.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
    }

