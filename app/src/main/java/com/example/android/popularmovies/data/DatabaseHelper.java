package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adaobifrank on 5/17/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVOURITE_TABLE =
                "CREATE TABLE "+ DatabaseContract.FavouriteEntry.TABLE_NAME + "("+
                        DatabaseContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DatabaseContract.FavouriteEntry.MOVIE_ID + " INTEGER UNIQUE, " +
                        DatabaseContract.FavouriteEntry.ORIGINAL_TITLE + " TEXT NOT NULL, "+
                        DatabaseContract.FavouriteEntry.OVERVIEW + " TEXT NOT NULL, "+
                        DatabaseContract.FavouriteEntry.POSTER_PATH + " TEXT NOT NULL, "+
                        DatabaseContract.FavouriteEntry.BACKDROP_PATH + " TEXT NOT NULL, "+
                        DatabaseContract.FavouriteEntry.RELEASE_DATE + " TEXT NOT NULL, "+
                        DatabaseContract.FavouriteEntry.VOTE_AVERAGE + " REAL NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DatabaseContract.FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
