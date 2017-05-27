package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.fragments.ReviewFragment;
import com.example.android.popularmovies.fragments.TrailerFragment;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adaobifrank on 5/18/17.
 */

public class MovieDetailsAdapter extends FragmentStatePagerAdapter {

    private Context context;

    private String[] tabTitles;
   // private List<Fragment> mFragments;
    private List<Review> reviews = new ArrayList<>();
    private List<Trailer> trailers  = new ArrayList<>();

    private static final int TRAILER = 0;
    private static final int REVIEW = 1;

    public MovieDetailsAdapter(FragmentManager fm, Context context, List<Trailer> trailers, List<Review> reviews){
        super(fm);
        this.context = context;
        this.reviews = reviews;
        this.trailers = trailers;
        this.tabTitles = new String[] { context.getString(R.string.movie_trailers_title), context.getString(R.string.movie_reviews_title) };
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position){
            case TRAILER:
                TrailerFragment trailerFragment = new TrailerFragment();
                trailerFragment.setMovieTrailers(trailers);
               return trailerFragment;
            case REVIEW:
                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setReviewList(reviews);
                return reviewFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

//    public View getTabView(int position) {
//        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
//        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//        TextView tv = (TextView) v.findViewById(R.id.textView);
//        tv.setText(tabTitles[position]);
//        ImageView img = (ImageView) v.findViewById(R.id.imgView);
//        img.setImageResource(imageResId[position]);
//        return v;
//    }
}
