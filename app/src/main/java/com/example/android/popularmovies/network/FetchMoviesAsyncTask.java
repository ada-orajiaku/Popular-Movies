package com.example.android.popularmovies.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adaobifrank on 4/12/17.
 */

public class FetchMoviesAsyncTask extends AsyncTask<String, Void, Movie[]> {

    private final AsyncTaskCallBack movieTaskCallback;
    private Context context;

    public FetchMoviesAsyncTask(Context context, AsyncTaskCallBack movieTaskCallback) {
        this.context = context;
        this.movieTaskCallback = movieTaskCallback;
    }


    private Movie[] deSearializeMovies(String movieJsonString) throws JSONException {

        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String BACKDROP_PATH = "backdrop_path";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";


        if (movieJsonString == null || "".equals(movieJsonString)) {
            return null;
        }

        JSONObject jsonObjectMovie = new JSONObject(movieJsonString);
        JSONArray jsonArrayMovies = jsonObjectMovie.getJSONArray("results");

        Movie[] movies = new Movie[jsonArrayMovies.length()];

//        for (int i = 0; i < jsonArrayMovies.length(); i++) {
//            JSONObject object = jsonArrayMovies.getJSONObject(i);
//            movies[i] = new Movie(object.getString(ORIGINAL_TITLE),
//                    object.getString(POSTER_PATH),
//                    object.getString(OVERVIEW),
//                    object.getString(VOTE_AVERAGE),
//                    object.getString(RELEASE_DATE));
//        }
        return movies;
    }


    @Override
    protected Movie[] doInBackground(String... params) {

        final String BASE_URL = "api_base_url";
        final String API_KEY = "api_key";


        HttpURLConnection urlConnection = null;
        String movieJsonString = null;
        BufferedReader reader = null;

        Uri uri = Uri.parse(context.getString(R.string.api_base_url)).buildUpon()
                .appendQueryParameter(params[0], params[1])
                .appendQueryParameter(API_KEY, context.getString(R.string.api_key))
                .build();

        try {
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader bufferedReader = null;

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while((json = bufferedReader.readLine())!= null){
                    sb.append(json+"\n");
                }

            movieJsonString = sb.toString().trim();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return deSearializeMovies(movieJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Movie[] movies) {
        movieTaskCallback.response(movies);
    }

    public interface AsyncTaskCallBack {

        void response(Movie[] movies);
    }
}
