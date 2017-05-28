package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by adaobifrank on 5/17/17.
 */

public class FavoriteDbProvider extends ContentProvider {

    private static final String TAG = FavoriteDbProvider.class.getSimpleName();
    public static final int CODE_FAVOURITE = 123;
    public static final int CODE_FAVOURITE_BY_ID = 125;

    private static final UriMatcher sUriMatcher = builderUriMatcher();
    private FavoriteDbHelper dbHelper;

    public static UriMatcher builderUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteDbContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FavoriteDbContract.PATH_FAVOURITE,CODE_FAVOURITE);
        uriMatcher.addURI(authority, FavoriteDbContract.PATH_FAVOURITE + "/#",CODE_FAVOURITE_BY_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case CODE_FAVOURITE_BY_ID:
                String movieId = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{movieId};

                cursor = dbHelper.getReadableDatabase().query(FavoriteDbContract.FavouriteEntry.TABLE_NAME,projection,
                        FavoriteDbContract.FavouriteEntry.MOVIE_ID +" = ?",selectionArguments,null,null,null);
                break;
            case CODE_FAVOURITE:
                cursor = dbHelper.getReadableDatabase().query(FavoriteDbContract.FavouriteEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : "+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri returnUri = null;

        switch (sUriMatcher.match(uri)){
            case CODE_FAVOURITE:
                long id = dbHelper.getWritableDatabase().insert(FavoriteDbContract.FavouriteEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FavoriteDbContract.FavouriteEntry.CONTENT_URI,id);
                }else {
                    Log.e(TAG,"Failed to insert row into " + uri);
                    //throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                Log.e(TAG,"Unknown uri : "+uri);
                //throw new UnsupportedOperationException("Unknown uri : "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;

        if(null == selection) selection ="1";

        switch (sUriMatcher.match(uri)){
            case CODE_FAVOURITE_BY_ID:
                String movieId = uri.getLastPathSegment();
                String[] whereArgs = new String[]{movieId};

                numRowsDeleted = dbHelper.getWritableDatabase().delete(FavoriteDbContract.FavouriteEntry.TABLE_NAME,
                        FavoriteDbContract.FavouriteEntry.MOVIE_ID+" =?", whereArgs);
                break;
            case CODE_FAVOURITE:
                numRowsDeleted = dbHelper.getWritableDatabase().delete(FavoriteDbContract.FavouriteEntry.TABLE_NAME,
                        selection,selectionArgs);
                break;
            default:
                Log.e(TAG,"Unknown uri : "+uri);
                //throw new UnsupportedOperationException("Unknown uri : "+uri);
        }

        if(numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db =dbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case CODE_FAVOURITE:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values){
                        long _id = db.insert(FavoriteDbContract.FavouriteEntry.TABLE_NAME,null,value);
                        if(_id != -1){
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri,values);
        }
    }
}
