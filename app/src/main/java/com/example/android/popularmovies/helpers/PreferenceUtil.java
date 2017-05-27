package com.example.android.popularmovies.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.popularmovies.R;

/**
 * Created by adaobifrank on 5/27/17.
 */

public class PreferenceUtil {
    private Context context;
    private SharedPreferences sharedPref;

    public PreferenceUtil(Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(context.getString(R.string.poplar_movies_preference),
                Context.MODE_PRIVATE);
    }

    public void savePref(String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String readPrefString(String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public boolean readPrefBoolean(String key, boolean defaultValue) {
        return sharedPref.getBoolean(key, defaultValue);
    }


    public int readPrefInt(String key, int defaultValue) {
        return sharedPref.getInt(key, defaultValue);
    }
}
