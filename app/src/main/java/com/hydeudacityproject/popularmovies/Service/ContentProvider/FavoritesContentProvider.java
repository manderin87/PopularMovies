package com.hydeudacityproject.popularmovies.Service.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;

import java.util.HashMap;

/**
 * Created by jhyde on 11/13/2015.
 */
public class FavoritesContentProvider extends ContentProvider {

    private static final String TAG = FavoritesContentProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoritesDBHelper mHelper;

    private static final int FAVORITE = 100;
    private static final int FAVORITE_WITH_ID = 200;

    private HashMap<String, String> FAVORITES_PROJECTION_MAP = new HashMap<>();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoritesContract.FavoriteEntry.TABLE_FAVORITES, FAVORITE);
        matcher.addURI(authority, FavoritesContract.FavoriteEntry.TABLE_FAVORITES + "/#", FAVORITE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new FavoritesDBHelper(getContext());

        return (mHelper == null) ? false : true;
    }



    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        // A convenience class to help build the query
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(FavoritesContract.FavoriteEntry.TABLE_FAVORITES);

        switch(sUriMatcher.match(uri)) {
            case FAVORITE: {
                    qb.setProjectionMap(FAVORITES_PROJECTION_MAP);
                }
                break;
            case FAVORITE_WITH_ID: {
                    // If this is a request for an individual status, limit the result set to that ID
                    qb.appendWhere(MovieDetail.MovieDatabaseKeys.Id.keyName() + "=" + uri.getLastPathSegment());
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // Use our default sort order if none was specified
        String orderBy = TextUtils.isEmpty(sortOrder)
                ? FavoritesContract.FavoriteEntry.DEFAULT_SORT_ORDER
                : sortOrder;

        // Query the underlying database
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Notify the context's ContentResolver if the cursor result set changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor to the result set
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case FAVORITE: {
                return FavoritesContract.FavoriteEntry.CONTENT_DIR_TYPE;
            }
            case FAVORITE_WITH_ID: {
                return FavoritesContract.FavoriteEntry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "inserted data");

        int uri_type = sUriMatcher.match(uri);

        SQLiteDatabase db = mHelper.getWritableDatabase();

        long id = 0;
        switch (uri_type) {
            case FAVORITE: {
                    id = db.insertWithOnConflict(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                            null, values, SQLiteDatabase.CONFLICT_REPLACE);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(FavoritesContract.FavoriteEntry.TABLE_FAVORITES + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "deleted data");

        int uri_type = sUriMatcher.match(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows_deleted = 0;

        switch(uri_type) {
            case FAVORITE:
                rows_deleted = db.delete(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                        selection,
                        selectionArgs);
                break;

            case FAVORITE_WITH_ID:
                String id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)) {
                    rows_deleted =
                            db.delete(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                                MovieDetail.MovieDatabaseKeys.Id.keyName() + "=" + id,
                                null);
                } else {
                    rows_deleted =
                            db.delete(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                                    MovieDetail.MovieDatabaseKeys.Id.keyName() + "=" + id
                                        + " and " +
                                        selection,
                                selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rows_deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uri_type = sUriMatcher.match(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows_updated = 0;

        switch (uri_type) {
            case FAVORITE:
                rows_updated = db.update(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                        values,
                        selection,
                        selectionArgs);
                break;
            case FAVORITE_WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rows_updated =
                            db.update(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                                    values,
                                    MovieDetail.MovieDatabaseKeys.Id.keyName() + "=" + id,
                                    null);
                } else {
                    rows_updated =
                            db.update(FavoritesContract.FavoriteEntry.TABLE_FAVORITES,
                                    values,
                                    MovieDetail.MovieDatabaseKeys.Id.keyName() + "=" + id
                                            + " and " +
                                            selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rows_updated;
    }
}
