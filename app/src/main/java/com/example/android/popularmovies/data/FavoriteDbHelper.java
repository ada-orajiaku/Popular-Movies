package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adaobifrank on 5/17/17.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVOURITE_TABLE =
                "CREATE TABLE "+ FavoriteDbContract.FavouriteEntry.TABLE_NAME + "("+
                        FavoriteDbContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteDbContract.FavouriteEntry.MOVIE_ID + " INTEGER UNIQUE, " +
                        FavoriteDbContract.FavouriteEntry.TITLE + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.POSTER_PATH + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.OVERVIEW + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.RELEASE_DATE + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.BACKDROP_PATH + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.POPULARITY + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.VOTE_COUNT + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.VIDEO + " TEXT NOT NULL, "+
                        FavoriteDbContract.FavouriteEntry.VOTE_AVERAGE + " REAL NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavoriteDbContract.FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
