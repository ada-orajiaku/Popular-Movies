package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmovies.helpers.NetworkUtil;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.network.ApiInterface;
import com.example.android.popularmovies.network.FetchMoviesAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private final Context context = this;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieRecyclerView;
    private List<Movie> movieArrayList = new ArrayList<Movie>();
    final String SORT_BY_TAG = "sort_by";
    final String SORT_BY_POPULARITY = "sort_by_popularity";
    final String SORT_BY_RATINGS = "sort_by_ratings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


                /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMovieRecyclerView.setHasFixedSize(true);

        FetchMovies(SORT_BY_TAG,this.getString(R.string.sort_by_popularity));
    }

    private void FetchMovies(String param, String url){

        ApiInterface service = NetworkUtil.getClient(context).create(ApiInterface.class);
        
        Call<MovieResponse> call;
        if (param == SORT_BY_RATINGS) {
            call = service.getTopRatedMovies(getString(R.string.api_key));
        } else {
            call = service.getPopularMovies(getString((R.string.api_key)));
        }

     //   moviesLoader.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                movieArrayList.clear();
                movieArrayList = response.body().getResults();
                if (movieArrayList != null && movieArrayList.size() > 0) {

                    mAdapter = new MovieAdapter(context, movieArrayList);
                    mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Movie movie) {
                            Toast.makeText(MainActivity.this, movie.getTitle(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(context, MovieDetailActivity.class);
                            intent.putExtra(context.getString(R.string.movie_package_name), movie);
                            startActivity(intent);
                        }
                    });
                    mMovieRecyclerView.setAdapter(mAdapter);
                }
              //  moviesLoader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
               // moviesLoader.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_most_popular) {
            Toast.makeText(this,R.string.notification_sort_by_most_popular,Toast.LENGTH_LONG).show();
            FetchMovies(SORT_BY_TAG,this.getString(R.string.sort_by_popularity));
            return true;
        }
        if (id == R.id.action_sort_by_top_rated) {
            Toast.makeText(this,R.string.notification_sort_by_top_rated,Toast.LENGTH_LONG).show();
            FetchMovies(SORT_BY_TAG,this.getString(R.string.sort_by_ratings));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
