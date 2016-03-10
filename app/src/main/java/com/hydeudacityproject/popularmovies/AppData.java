package com.hydeudacityproject.popularmovies;

import com.hydeudacityproject.popularmovies.Service.ContentProvider.FavoritesDBHelper;
import com.hydeudacityproject.popularmovies.Service.Framework.Movie;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.lonewolfgames.framework.AbstractAppData;
import com.lonewolfgames.framework.AbstractMainApplication;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class AppData extends AbstractAppData  implements AppSettings.AppSettingsLoadedListener {

    private FavoritesDBHelper mHelper;

    public AppData(AbstractMainApplication app) {
        super(app);

        mHelper = new FavoritesDBHelper(mApp);
    }

    public static AppData getInstance() {
        return (AppData) mInstance;
    }

    @Override
    public void OnAppSettingsLoadingFinished() {

    }

    public ArrayList<Movie> favoriteMovies() {
        ArrayList<Movie> movies = new ArrayList<>();

        ArrayList<MovieDetail> movie_detail = mHelper.getAllFavorites();

        for (MovieDetail detail : movie_detail) {
            movies.add(detail);
        }

        return movies;
    }

    public boolean isFavorite(int id) {
        return mHelper.getFavorite(id) != null ? true : false;
    }

    public MovieDetail getFavorite(int id) {
        return mHelper.getFavorite(id);
    }

    public void addToFavorites(MovieDetail movie) {
        mHelper.addFavorite(movie);
    }

    public void removeFromFavorites(int id) {
        mHelper.deleteFavorite(id);
    }
}
