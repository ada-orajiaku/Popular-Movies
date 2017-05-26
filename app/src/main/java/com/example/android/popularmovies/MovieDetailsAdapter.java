package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.popularmovies.movie_trailer.TrailerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adaobifrank on 5/18/17.
 */

public class MovieDetailsAdapter extends FragmentPagerAdapter {

    private Context context;

    private String[] tabTitles;
    private List<Fragment> mFragments;

    public MovieDetailsAdapter(FragmentManager fm, Context context, List<Fragment> mFragments) {
        super(fm);
        this.context = context;
        this.mFragments = mFragments;
        this.tabTitles = new String[] { context.getString(R.string.movie_trailers_title), context.getString(R.string.movie_reviews_title) };
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
