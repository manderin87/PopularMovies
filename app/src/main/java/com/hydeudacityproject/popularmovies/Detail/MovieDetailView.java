package com.hydeudacityproject.popularmovies.Detail;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydeudacityproject.popularmovies.AppData;
import com.hydeudacityproject.popularmovies.MainApplication;
import com.hydeudacityproject.popularmovies.Movie.MovieView;
import com.hydeudacityproject.popularmovies.Movie.MovieViewFragment;
import com.hydeudacityproject.popularmovies.R;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieVideo;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.Cache.Images.ImageLoader;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class MovieDetailView extends AppCompatActivity {

    private static final String TAG = MovieDetailView.class.getSimpleName();

    private static final String TAG_FRAGMENT = "fragment";
    public static final String KEY_MOVIE_DETAIL = "movie_detail";

    private MainApplication mApp;
    private MovieDetailViewFragment mMovieDetailViewFragment;
    private MovieDetail mMovieDetail = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail_view);

        mApp = (MainApplication) getApplicationContext();

        if(getIntent().hasExtra(MovieViewFragment.KEY_MOVIE_DETAIL)) {
            mMovieDetail = getIntent().getParcelableExtra(MovieViewFragment.KEY_MOVIE_DETAIL);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_DETAIL)) {
            mMovieDetail = savedInstanceState.getParcelable(KEY_MOVIE_DETAIL);
        }

        if(savedInstanceState == null) {
            mMovieDetailViewFragment = MovieDetailViewFragment.newInstance(mMovieDetail);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_movie_detail_view, mMovieDetailViewFragment, TAG_FRAGMENT).commit();
        } else {
            mMovieDetailViewFragment = (MovieDetailViewFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_MOVIE_DETAIL, mMovieDetail);
    }
}
