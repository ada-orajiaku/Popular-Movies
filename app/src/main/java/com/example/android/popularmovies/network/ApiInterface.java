package com.example.android.popularmovies.network;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MovieResponse;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.ReviewResponse;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by adaobifrank on 5/17/17.
 */

public interface ApiInterface {

    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query(ApiConstants.API_KEY) String apiKey);

    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query(ApiConstants.API_KEY) String apiKey);

    @GET("{id}")
    Call<Movie> getMovieDetails(@Path(ApiConstants.ID) int id, @Query(ApiConstants.API_KEY) String apiKey);

    @GET("{movie_id}/reviews")
    Call<ReviewResponse> getReviews(@Path(ApiConstants.MOVIE_ID) int movieId, @Query(ApiConstants.API_KEY) String apiKey);

    @GET("{movie_id}/videos")
    Call<TrailerResponse> getTrailers(@Path(ApiConstants.MOVIE_ID) int movieId, @Query(ApiConstants.API_KEY) String apiKey);
}
