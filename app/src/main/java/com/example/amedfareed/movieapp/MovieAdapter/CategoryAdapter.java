package com.example.amedfareed.movieapp.MovieAdapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.amedfareed.movieapp.activity.InfoFragment;
import com.example.amedfareed.movieapp.activity.ReviewsFragment;
import com.example.amedfareed.movieapp.activity.TrailersFragment;

/**
 * Created by amedfareed on 25/04/18.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private String[] tabCategories = new String[]{"info", "trailers", "reviews"};

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            return new InfoFragment();
        } else if (position == 1) {
            return new TrailersFragment();
        } else {
            return new ReviewsFragment();
        }
    }



    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return tabCategories[position];
    }
}
