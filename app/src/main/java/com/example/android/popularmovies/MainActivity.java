package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Context context = this;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieRecyclerView;
    private ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
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

        FetchMovies(SORT_BY_TAG,Helper.getMetaData(this,SORT_BY_POPULARITY));
    }

    private void FetchMovies(String param, String url){
        FetchMoviesAsyncTask moviesTask = new FetchMoviesAsyncTask(this,new FetchMoviesAsyncTask.AsyncTaskCallBack() {
            @Override
            public void response(Movie[] movies) {
                if (movies != null) {
                    Collections.addAll(movieArrayList, movies);
                    List<Movie> movieResponseList = new ArrayList<Movie>(Arrays.asList(movies));
                    mAdapter = new MovieAdapter(context, movieResponseList);
                    mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Movie movie) {
                            Toast.makeText(MainActivity.this, movie.getTitle(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(context, MovieDetailActivity.class);
                            intent.putExtra("com.example.android.popularmovies.Movie", movie);
                            startActivity(intent);
                        }
                    });
                    mMovieRecyclerView.setAdapter(mAdapter);
                }
            }
        });
        moviesTask.execute(param,url);
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
            FetchMovies(SORT_BY_TAG,Helper.getMetaData(this,SORT_BY_POPULARITY));
            return true;
        }
        if (id == R.id.action_sort_by_top_rated) {
            Toast.makeText(this,R.string.notification_sort_by_top_rated,Toast.LENGTH_LONG).show();
            FetchMovies(SORT_BY_TAG,Helper.getMetaData(this,SORT_BY_RATINGS));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
