package com.example.android.popularmovies.helpers;

import android.content.Context;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.network.ApiConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adaobifrank on 5/17/17.
 */

public class NetworkUtil {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context){
        if(retrofit == null){
            retrofit = new  Retrofit.Builder()
                    .baseUrl(context.getString(R.string.api_base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}