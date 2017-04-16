package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL = "image_base_url";
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
        Movie movie = bundle.getParcelable("com.example.android.popularmovies.Movie");

        if (movie != null) {

            getSupportActionBar().setTitle(movie.getTitle());
            toolbar.setTitle(movie.getTitle());
            Picasso.with(this).load(Helper.getMetaData(this,IMAGE_BASE_URL)+movie.getPosterPath()).into(posterImage);
            movieTitle.setText(movie.getTitle());
            movieReleaseDate.setText(movie.getReleaseDate());
            movieAverageRating.setText(movie.getVoteAverage());
            moviePlot.setText(movie.getOverview());
        }

    }
}
