package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.helpers.NetworkUtil;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.helpers.MetaDataUtil;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.ReviewResponse;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.model.TrailerResponse;
import com.example.android.popularmovies.movie_review.ReviewFragment;
import com.example.android.popularmovies.movie_trailer.TrailerFragment;
import com.example.android.popularmovies.network.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerFragment.OnListFragmentInteractionListener,
        ReviewFragment.OnListFragmentInteractionListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView posterImage = (ImageView) findViewById(R.id.movie_poster);
        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        TextView movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        TextView movieAverageRating = (TextView) findViewById(R.id.movie_vote_average);
        TextView moviePlot = (TextView) findViewById(R.id.movie_plot_synopsis);

        Bundle bundle = getIntent().getExtras();
        this.movie = bundle.getParcelable(this.getString(R.string.movie_package_name));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);


        if (movie != null)
        {
            getSupportActionBar().setTitle(movie.getTitle());
            toolbar.setTitle(movie.getTitle());
            Picasso.with(this).load(getString(R.string.image_base_url_500)+movie.getBackdropPath()).into(posterImage);
            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieAverageRating.setText(movie.getVoteAverage());
            moviePlot.setText(movie.getOverview());

            service = NetworkUtil.getClient(this).create(ApiInterface.class);

            loadTrailers();
            loadReviews();
            // Get the ViewPager and set it's PagerAdapter so that it can display items
        }
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
                //  moviesLoader.setVisibility(View.INVISIBLE);
                loadFragments(trailersList, reviewList);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                // moviesLoader.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void loadFragments(List<Trailer> trailers, List<Review> reviews){

        if(trailers != null && reviews != null)
        {
            mFragments = new ArrayList<>();
            mFragments.add(TrailerFragment.newInstance(trailersList,this));
            mFragments.add(ReviewFragment.newInstance(reviewList));

            viewPager.setAdapter(new MovieDetailsAdapter(this.getSupportFragmentManager(), this, mFragments));
            tabLayout.setupWithViewPager(viewPager);
        }
    }
}
