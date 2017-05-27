package com.example.android.popularmovies.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.FavoriteDbContract;
import com.example.android.popularmovies.helpers.NetworkUtil;
import com.example.android.popularmovies.helpers.PreferenceUtil;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.MovieResponse;
import com.example.android.popularmovies.network.ApiInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "";
    private final Context context = this;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieRecyclerView;
    private List<Movie> movieArrayList = new ArrayList<Movie>();
    final String SORT_BY_TAG = "sort_by";
    final String SORT_BY_POPULARITY = "sort_by_popularity";
    final String SORT_BY_RATINGS = "sort_by_ratings";
    final static int MOVIE_LOADER_TAG = 8947;

    ProgressDialog progressdialog;

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

        mAdapter = new MovieAdapter(context);
        mAdapter.setMovieList(new ArrayList<Movie>());

        mMovieRecyclerView.setAdapter(mAdapter);

        progressdialog = new ProgressDialog(MainActivity.this);
        progressdialog.setCancelable(false);
        progressdialog.setMessage("Please Wait....");

        FetchMovies(SORT_BY_POPULARITY,this.getString(R.string.sort_by_popularity));
    }

    private void FetchMovies(String param, String url){

        ApiInterface service = NetworkUtil.getClient(context).create(ApiInterface.class);
        
        Call<MovieResponse> call;
        if (param == SORT_BY_RATINGS) {
            call = service.getTopRatedMovies(getString(R.string.api_key));
        } else {
            call = service.getPopularMovies(getString((R.string.api_key)));
        }

        progressdialog.show();

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                progressdialog.dismiss();
                movieArrayList.clear();
                movieArrayList = response.body().getResults();

                mAdapter.setMovieList(movieArrayList);
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Movie movie) {
                        Toast.makeText(MainActivity.this, movie.getTitle(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(context.getString(R.string.movie_package_name), movie);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.getMessage());
               progressdialog.dismiss();
                Toast.makeText(MainActivity.this,R.string.request_error,Toast.LENGTH_LONG).show();
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
            FetchMovies(SORT_BY_POPULARITY,this.getString(R.string.sort_by_popularity));
            return true;
        }
        if (id == R.id.action_sort_by_top_rated) {
            FetchMovies(SORT_BY_RATINGS,this.getString(R.string.sort_by_ratings));
            return true;
        }

        if (id == R.id.action_sort_by_favorites) {
            getSupportLoaderManager().initLoader(MOVIE_LOADER_TAG, null, MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIE_LOADER_TAG:
                Uri getAllFavourites = FavoriteDbContract.FavouriteEntry.CONTENT_URI;
                return new CursorLoader(this, getAllFavourites, null, null, null, null);
            default:
                throw new RuntimeException("Loader not implemented : " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setMovieList(movieCursorToList(data));
        mAdapter.notifyDataSetChanged();
    }

    public List<Movie> movieCursorToList(Cursor cursor) {
        List<Movie> movieList = new ArrayList<Movie>();

        while (cursor.moveToNext()) {

            int movieId = cursor.getInt(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.TITLE));
            String posterPath = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.POSTER_PATH));
            String overView = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.OVERVIEW));
            String releaseDate = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.RELEASE_DATE));
            String backdropPath = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.BACKDROP_PATH));
            String popularity = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.POPULARITY));
            String voteCount = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.VOTE_COUNT));
            String video = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.VIDEO));
            String voteAverage = cursor.getString(cursor.getColumnIndex(FavoriteDbContract.FavouriteEntry.VOTE_AVERAGE));

            Movie movie = new Movie(movieId, title, posterPath, overView, releaseDate, backdropPath, popularity, voteCount, video, voteAverage);
            movieList.add(movie);
        }
        return movieList;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
