package com.example.amedfareed.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class MovieContentProvider extends ContentProvider {
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final UriMatcher sUriMatcher =new UriMatcher(UriMatcher.NO_MATCH);

    private DbHelper myDbHelper;


    static {
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.MOVIES_PATH, MOVIE);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.MOVIES_PATH + "/#", MOVIE_WITH_ID);
    }
    @Override
    public boolean onCreate() {
        myDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch(match){
            case MOVIE:
                cursor = db.query(MoviesContract.MoviesDateBase.TABLE_NAME, projection, selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                selection = MoviesContract.MoviesDateBase._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor  = db.query(MoviesContract.MoviesDateBase.TABLE_NAME
                ,projection, selection, selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
        }
        assert cursor != null;
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        long insertedMovieId;
        Uri insertedMovieUri;
        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                insertedMovieId  = db.insert(MoviesContract.MoviesDateBase.TABLE_NAME,
                        null,
                        values);
                if(insertedMovieId > 0){
                    insertedMovieUri = Uri.parse(MoviesContract.MoviesDateBase._ID);
                }else {
                    throw  new UnsupportedOperationException("Unknown Uri" + uri);
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return insertedMovieUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        int deletedMovieId;
        int match = sUriMatcher.match(uri);
        if(selection == null){
            selection = "1";
        }
        switch (match){
            case MOVIE:
                deletedMovieId = db.delete(MoviesContract.MoviesDateBase.TABLE_NAME,
                        selection,
                        selectionArgs
                        );
                break;
                default:
                    throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        if(deletedMovieId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedMovieId;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
