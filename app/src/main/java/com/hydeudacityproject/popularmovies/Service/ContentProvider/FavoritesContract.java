package com.hydeudacityproject.popularmovies.Service.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;

/**
 * Created by jhyde on 11/13/2015.
 */
public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "com.hydeudacityproject.popularmovies.Service.ContentProvider.FavoritesContentProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoriteEntry implements BaseColumns {
        // table name
        public static final String TABLE_FAVORITES = "favorites";
        // columns
        public static final String CREATE_TABLE = "CREATE TABLE " +
                FavoritesContract.FavoriteEntry.TABLE_FAVORITES + "(" +
                MovieDetail.MovieDatabaseKeys._Id.keyName() + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieDetail.MovieDatabaseKeys.Adult.keyName() + " INTEGER NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.BackdropPath.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.GenreIds.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Id.keyName() + " INTEGER NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.OriginalLanguage.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.OriginalTitle.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Overview.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.ReleaseDate.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.PosterPath.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Popularity.keyName() + " REAL NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Title.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Video.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.VoteAverage.keyName() + " REAL NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.VoteCount.keyName() + " INTEGER NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Budget.keyName() + " INTEGER NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Genres.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.IMDBId.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.ProductionCompanies.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.ProductionCountries.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Revenue.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Runtime.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.SpokenLanguages.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Status.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Tagline.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Videos.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.VideosLoaded.keyName() + " INTEGER NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.Reviews.keyName() + " TEXT NOT NULL, " +
                MovieDetail.MovieDatabaseKeys.ReviewsLoaded.keyName() + " INTEGER NOT NULL);";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_FAVORITES;

        public static final String DEFAULT_SORT_ORDER = MovieDetail.MovieDatabaseKeys.Id.keyName() + " ASC";

        // content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_FAVORITES).build();
        // cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;
        // cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVORITES;

        public static Uri buildFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
