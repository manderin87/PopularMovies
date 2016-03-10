package com.hydeudacityproject.popularmovies.Service.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/13/2015.
 */
public class FavoritesDBHelper extends SQLiteOpenHelper {

    private static final String TAG = FavoritesDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    private ContentResolver mContentResolver;

    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FavoritesContract.FavoriteEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(FavoritesContract.FavoriteEntry.DELETE_TABLE);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + FavoritesContract.FavoriteEntry.TABLE_FAVORITES + "'");

        onCreate(db);
    }



    public void addFavorite(MovieDetail movie) {

        ContentValues values = new ContentValues();

        //values.put(MovieDetail.MovieDatabaseKeys._Id.keyName(), 0);
        values.put(MovieDetail.MovieDatabaseKeys.Adult.keyName(), movie.adult());
        values.put(MovieDetail.MovieDatabaseKeys.BackdropPath.keyName(), movie.backdropPath());
        values.put(MovieDetail.MovieDatabaseKeys.GenreIds.keyName(), movie.genreIds());
        values.put(MovieDetail.MovieDatabaseKeys.Id.keyName(), movie.id());
        values.put(MovieDetail.MovieDatabaseKeys.OriginalLanguage.keyName(), movie.originalLanguage());
        values.put(MovieDetail.MovieDatabaseKeys.OriginalTitle.keyName(), movie.originalTitle());
        values.put(MovieDetail.MovieDatabaseKeys.Overview.keyName(), movie.overview());
        values.put(MovieDetail.MovieDatabaseKeys.ReleaseDate.keyName(), movie.releaseDateAsString());
        values.put(MovieDetail.MovieDatabaseKeys.PosterPath.keyName(), movie.posterPath());
        values.put(MovieDetail.MovieDatabaseKeys.Popularity.keyName(), movie.popularity());
        values.put(MovieDetail.MovieDatabaseKeys.Title.keyName(), movie.title());
        values.put(MovieDetail.MovieDatabaseKeys.Video.keyName(), movie.video());
        values.put(MovieDetail.MovieDatabaseKeys.VoteAverage.keyName(), movie.voteAverage());
        values.put(MovieDetail.MovieDatabaseKeys.VoteCount.keyName(), movie.voteCount());
        values.put(MovieDetail.MovieDatabaseKeys.Budget.keyName(), movie.budget());
        values.put(MovieDetail.MovieDatabaseKeys.Genres.keyName(), movie.genresInformation());
        values.put(MovieDetail.MovieDatabaseKeys.IMDBId.keyName(), movie.idmbId());
        values.put(MovieDetail.MovieDatabaseKeys.ProductionCompanies.keyName(), movie.productionCompaniesInformation());
        values.put(MovieDetail.MovieDatabaseKeys.ProductionCountries.keyName(), movie.productionCountriesInformation());
        values.put(MovieDetail.MovieDatabaseKeys.Revenue.keyName(), movie.revenue());
        values.put(MovieDetail.MovieDatabaseKeys.Runtime.keyName(), movie.runtime());
        values.put(MovieDetail.MovieDatabaseKeys.SpokenLanguages.keyName(), movie.spokenLanguagesInformation());
        values.put(MovieDetail.MovieDatabaseKeys.Status.keyName(), movie.status());
        values.put(MovieDetail.MovieDatabaseKeys.Tagline.keyName(), movie.tagline());
        values.put(MovieDetail.MovieDatabaseKeys.Videos.keyName(), movie.videosInformation());
        values.put(MovieDetail.MovieDatabaseKeys.VideosLoaded.keyName(), movie.isVideosLoaded());
        values.put(MovieDetail.MovieDatabaseKeys.Reviews.keyName(), movie.reviewsInformation());
        values.put(MovieDetail.MovieDatabaseKeys.ReviewsLoaded.keyName(), movie.isReviewsLoaded());

        mContentResolver.insert(FavoritesContract.FavoriteEntry.CONTENT_URI, values);
    }

    public MovieDetail getFavorite(int id) {
//        String[] projection = {
//                MovieDetail.MovieDatabaseKeys._Id.keyName(),
//                MovieDetail.MovieDatabaseKeys.Adult.keyName(),
//                MovieDetail.MovieDatabaseKeys.BackdropPath.keyName(),
//                MovieDetail.MovieDatabaseKeys.GenreIds.keyName(),
//                MovieDetail.MovieDatabaseKeys.Id.keyName(),
//                MovieDetail.MovieDatabaseKeys.OriginalLanguage.keyName(),
//                MovieDetail.MovieDatabaseKeys.OriginalTitle.keyName(),
//                MovieDetail.MovieDatabaseKeys.Overview.keyName(),
//                MovieDetail.MovieDatabaseKeys.ReleaseDate.keyName(),
//                MovieDetail.MovieDatabaseKeys.PosterPath.keyName(),
//                MovieDetail.MovieDatabaseKeys.Popularity.keyName(),
//                MovieDetail.MovieDatabaseKeys.Title.keyName(),
//                MovieDetail.MovieDatabaseKeys.Video.keyName(),
//                MovieDetail.MovieDatabaseKeys.VoteAverage.keyName(),
//                MovieDetail.MovieDatabaseKeys.VoteCount.keyName(),
//                MovieDetail.MovieDatabaseKeys.Budget.keyName(),
//                MovieDetail.MovieDatabaseKeys.Genres.keyName(),
//                MovieDetail.MovieDatabaseKeys.IMDBId.keyName(),
//                MovieDetail.MovieDatabaseKeys.ProductionCompanies.keyName(),
//                MovieDetail.MovieDatabaseKeys.ProductionCountries.keyName(),
//                MovieDetail.MovieDatabaseKeys.Revenue.keyName(),
//                MovieDetail.MovieDatabaseKeys.Runtime.keyName(),
//                MovieDetail.MovieDatabaseKeys.SpokenLanguages.keyName(),
//                MovieDetail.MovieDatabaseKeys.Status.keyName(),
//                MovieDetail.MovieDatabaseKeys.Tagline.keyName()};

        String selection = MovieDetail.MovieDatabaseKeys.Id.keyName() + " = \"" + String.valueOf(id) + "\"";

        Cursor cursor = mContentResolver.query(FavoritesContract.FavoriteEntry.CONTENT_URI,
                null, selection, null,
                null);

        MovieDetail.Builder movie = new MovieDetail.Builder();

        if(cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            movie.fromCursor(cursor);
        } else {
            return null;
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return movie.build();
    }

    public ArrayList<MovieDetail> getAllFavorites() {
        ArrayList<MovieDetail> movies = new ArrayList<>();

//        String[] projection = {
//                MovieDetail.MovieDatabaseKeys._Id.keyName(),
//                MovieDetail.MovieDatabaseKeys.Adult.keyName(),
//                MovieDetail.MovieDatabaseKeys.BackdropPath.keyName(),
//                MovieDetail.MovieDatabaseKeys.GenreIds.keyName(),
//                MovieDetail.MovieDatabaseKeys.Id.keyName(),
//                MovieDetail.MovieDatabaseKeys.OriginalLanguage.keyName(),
//                MovieDetail.MovieDatabaseKeys.OriginalTitle.keyName(),
//                MovieDetail.MovieDatabaseKeys.Overview.keyName(),
//                MovieDetail.MovieDatabaseKeys.ReleaseDate.keyName(),
//                MovieDetail.MovieDatabaseKeys.PosterPath.keyName(),
//                MovieDetail.MovieDatabaseKeys.Popularity.keyName(),
//                MovieDetail.MovieDatabaseKeys.Title.keyName(),
//                MovieDetail.MovieDatabaseKeys.Video.keyName(),
//                MovieDetail.MovieDatabaseKeys.VoteAverage.keyName(),
//                MovieDetail.MovieDatabaseKeys.VoteCount.keyName(),
//                MovieDetail.MovieDatabaseKeys.Budget.keyName(),
//                MovieDetail.MovieDatabaseKeys.Genres.keyName(),
//                MovieDetail.MovieDatabaseKeys.IMDBId.keyName(),
//                MovieDetail.MovieDatabaseKeys.ProductionCompanies.keyName(),
//                MovieDetail.MovieDatabaseKeys.ProductionCountries.keyName(),
//                MovieDetail.MovieDatabaseKeys.Revenue.keyName(),
//                MovieDetail.MovieDatabaseKeys.Runtime.keyName(),
//                MovieDetail.MovieDatabaseKeys.SpokenLanguages.keyName(),
//                MovieDetail.MovieDatabaseKeys.Status.keyName(),
//                MovieDetail.MovieDatabaseKeys.Tagline.keyName()};



        Cursor cursor = mContentResolver.query(FavoritesContract.FavoriteEntry.CONTENT_URI,
                null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                movies.add(new MovieDetail.Builder().fromCursor(cursor).build());
            } while(cursor.moveToNext());

            if(!cursor.isClosed()) {
                cursor.close();
            }
        }

        return movies;
    }

    public boolean deleteFavorite(int id) {

        boolean result = false;

        String selection = MovieDetail.MovieDatabaseKeys.Id.keyName() + " = \"" + String.valueOf(id) + "\"";

        int rowsDeleted = mContentResolver.delete(FavoritesContract.FavoriteEntry.CONTENT_URI,
                selection, null);

        if (rowsDeleted > 0)
            result = true;

        return result;
    }
}
