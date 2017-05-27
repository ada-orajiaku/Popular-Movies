package com.example.android.popularmovies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.MovieDetailsAdapter;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.FavoriteDbContract;
import com.example.android.popularmovies.helpers.NetworkUtil;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.ReviewResponse;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.models.TrailerResponse;
import com.example.android.popularmovies.fragments.ReviewFragment;
import com.example.android.popularmovies.fragments.TrailerFragment;
import com.example.android.popularmovies.network.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerFragment.OnListFragmentInteractionListener,
        ReviewFragment.OnListFragmentInteractionListener, TabLayout.OnTabSelectedListener {

    private static final String IMAGE_BASE_URL = "image_base_url_500";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private ArrayList<Fragment> mFragments;
    public TrailerFragment.OnListFragmentInteractionListener mTrailerListener;

    private Movie movie;
    private List<Trailer> trailersList;
    private List<Review> reviewList;

    private static final int TRAILER_POSITION = 0;
    TrailerFragment trailerFragment;
    private static final int REVIEW_POSITION = 1;
    ReviewFragment reviewFragment;

    ApiInterface service;

    ViewPager viewPager;
    TabLayout tabLayout;

    private static final int TRAILER = 0;
    private static final int REVIEW = 1;

    private Uri uri;
    boolean isFavorite;
    ImageButton favouriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView posterImage = (ImageView) findViewById(R.id.movie_poster);
        TextView movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        TextView movieAverageRating = (TextView) findViewById(R.id.movie_vote_average);
        TextView moviePlot = (TextView) findViewById(R.id.movie_plot_synopsis);
        favouriteButton = (ImageButton)findViewById(R.id.favourite_button);

        Bundle bundle = getIntent().getExtras();
        this.movie = bundle.getParcelable(this.getString(R.string.movie_package_name));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);


        if (movie != null)
        {
            getSupportActionBar().setTitle(movie.getTitle());
            toolbar.setTitle(movie.getTitle());
            Picasso.with(this).load(getString(R.string.image_base_url_500)+movie.getPosterPath()).into(posterImage);
            movieReleaseDate.setText(movie.getReleaseDate());
            movieAverageRating.setText(movie.getVoteAverage());
            moviePlot.setText(movie.getOverview());

            service = NetworkUtil.getClient(this).create(ApiInterface.class);

            loadTrailers();
            loadReviews();
            // Get the ViewPager and set it's PagerAdapter so that it can display items
        }

        uri = FavoriteDbContract.FavouriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();

        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            isFavorite  = true;
            favouriteButton.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.star_filled));
        }

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isFavorite)
                {
                    int numDeleted = getContentResolver().delete(uri,null,null);
                    if(numDeleted > 0){
                        favouriteButton.setImageDrawable(ContextCompat.getDrawable(MovieDetailActivity.this,R.drawable.ic_star_border_black_24dp));
                        Toast.makeText(MovieDetailActivity.this, "Unfavorited!", Toast.LENGTH_LONG).show();
                    }
                }else  {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(FavoriteDbContract.FavouriteEntry.MOVIE_ID, movie.getId());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.TITLE, movie.getTitle());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.POSTER_PATH, movie.getPosterPath());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.OVERVIEW, movie.getOverview());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.RELEASE_DATE, movie.getReleaseDate());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.BACKDROP_PATH, movie.getBackdropPath());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.POPULARITY, movie.getPopularity());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.VOTE_COUNT, movie.getVoteCount());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.VIDEO, movie.getVideo());
                    contentValues.put(FavoriteDbContract.FavouriteEntry.VOTE_AVERAGE, movie.getVoteAverage());

                    Uri uriResult = getContentResolver().insert(FavoriteDbContract.FavouriteEntry.CONTENT_URI, contentValues);

                    if (uriResult != null) {
                        favouriteButton.setImageDrawable(ContextCompat.getDrawable(MovieDetailActivity.this,R.drawable.ic_star_black_24dp));
                        Toast.makeText(MovieDetailActivity.this, "Favorited!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("Hi, checkout"+movie.getTitle()+", and it is rated "+movie.getVoteAverage()));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    @Override
    public void onListFragmentInteraction(Review review) {

    }

    @Override
    public void onListFragmentInteraction(Trailer trailer) {

    }


    private void loadTrailers(){

        Call<TrailerResponse> call = service.getTrailers(movie.getId(), getString(R.string.api_key));
        call.enqueue(new Callback<TrailerResponse>()
        {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if(trailersList == null)
                    trailersList = new ArrayList<>();

                trailersList.clear();
                trailersList = response.body().getResults();
                loadFragments(trailersList, reviewList);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                Toast.makeText(MovieDetailActivity.this,R.string.request_error,Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadReviews(){

        Call<ReviewResponse> call = service.getReviews(movie.getId(),this.getString(R.string.api_key));
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if(reviewList == null)
                    reviewList = new ArrayList<>();

                reviewList.clear();
                reviewList = response.body().getResults();
                loadFragments(trailersList, reviewList);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(MovieDetailActivity.this,R.string.request_error,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadFragments(List<Trailer> trailers, List<Review> reviews){

        if(trailers != null && reviews != null)
        {
            MovieDetailsAdapter pagerAdapter = new MovieDetailsAdapter(this.getSupportFragmentManager() ,this.getApplicationContext(), trailers,reviews);
            viewPager.setAdapter(pagerAdapter);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });
//            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(this);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition())
        {
            case TRAILER:
                TrailerFragment trailerFragment = new TrailerFragment();
                trailerFragment.setMovieTrailers(trailersList);

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(trailerFragment,TrailerFragment.class.getSimpleName())
                        .replace(R.id.content_layout,trailerFragment)
                        .commit();
                return;
            case REVIEW:
                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setReviewList(reviewList);

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(reviewFragment,ReviewFragment.class.getSimpleName())
                        .replace(R.id.content_layout,reviewFragment)
                        .commit();
                return;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
