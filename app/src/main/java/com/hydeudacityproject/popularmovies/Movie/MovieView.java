package com.hydeudacityproject.popularmovies.Movie;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.hydeudacityproject.popularmovies.AppData;
import com.hydeudacityproject.popularmovies.Detail.MovieDetailView;
import com.hydeudacityproject.popularmovies.Detail.MovieDetailViewFragment;
import com.hydeudacityproject.popularmovies.MainApplication;
import com.hydeudacityproject.popularmovies.R;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieVideo;
import com.hydeudacityproject.popularmovies.Service.MovieService.ResultType;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public class MovieView extends AppCompatActivity implements
        MovieViewFragment.MovieSelectedListener {

    private static final String TAG = MovieView.class.getSimpleName();

    private static final String TAG_MOVIE_VIEW_FRAGMENT = "movie_view_fragment";
    private static final String TAG_MOVIE_DETAIL_VIEW_FRAGMENT = "movie_detail_view_fragment";

//    public static final String KEY_MOVIE_DETAIL = "movie_detail";
//    public static final String KEY_MOVIE_POSTER_IMAGE_PATH = "movie_poster_image_path";
//    public static final String KEY_MOVIE_BACKDROP_IMAGE_PATH = "movie_backdrop_image_path";


    private MainApplication mApp;
    private static MovieView mInstance;


    private FrameLayout mMovieViewFrameLayout;
    private FrameLayout mMovieDetailViewFrameLayout;

    private MovieViewFragment mMovieViewFragment;
    private MovieDetailViewFragment mMovieDetailViewFragment;

    private boolean mShowDetailView = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_view);

        mInstance = this;

        mApp = (MainApplication) getApplicationContext();

        mMovieViewFrameLayout = (FrameLayout) findViewById(R.id.frameLayout_movie_view);
        mMovieDetailViewFrameLayout = (FrameLayout) findViewById(R.id.frameLayout_movie_detail_view);

        mMovieViewFragment = (MovieViewFragment) getFragmentManager().findFragmentByTag(TAG_MOVIE_VIEW_FRAGMENT);
        mMovieDetailViewFragment = (MovieDetailViewFragment) getFragmentManager().findFragmentByTag(TAG_MOVIE_DETAIL_VIEW_FRAGMENT);

        if(mMovieDetailViewFragment == null) {
            if(mMovieDetailViewFrameLayout != null && mShowDetailView == false) {
                mMovieDetailViewFrameLayout.setVisibility(View.GONE);
            }
        }

        if(savedInstanceState == null) {
            if(mMovieViewFrameLayout != null) {
                mMovieViewFragment = MovieViewFragment.newInstance();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout_movie_view, mMovieViewFragment, TAG_MOVIE_VIEW_FRAGMENT).commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    protected void onResume() {
        super.onResume();

        reloadFavorites();
    }



    @Override
    public void OnMovieSelected(View itemView, MovieDetail movie) {
        if(mMovieDetailViewFrameLayout == null) {
            View image_view = itemView.findViewById(R.id.imageView_movie_thumbnail);

            Intent intent = new Intent(itemView.getContext(), MovieDetailView.class);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) itemView.getContext(),
                    image_view,
                    image_view.getTransitionName()
            );

            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieViewFragment.KEY_MOVIE_DETAIL, movie);
            bundle.putString(MovieViewFragment.KEY_MOVIE_POSTER_IMAGE_PATH, movie.posterImage());
            bundle.putString(MovieViewFragment.KEY_MOVIE_BACKDROP_IMAGE_PATH, movie.backdropImage());

            intent.putExtras(bundle);
            ActivityCompat.startActivity((Activity) itemView.getContext(), intent, options.toBundle());
        } else {
            mMovieDetailViewFrameLayout.setVisibility(View.VISIBLE);
            mShowDetailView = true;
//            getFragmentManager()
//                    .beginTransaction()
//                    .detach(mMovieViewFragment)
//                    .attach(mMovieViewFragment)
//                    .commit();

            mMovieDetailViewFragment = MovieDetailViewFragment.newInstance(movie);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_movie_detail_view, mMovieDetailViewFragment, TAG_MOVIE_DETAIL_VIEW_FRAGMENT)
                    .commit();
        }
    }

    public void reloadFavorites() {
        if(mMovieViewFragment != null) {
            mMovieViewFragment.reloadFavorites();
        }
    }

    public boolean isTabletView() {
        return mMovieDetailViewFragment != null;
    }

    public boolean isDetailVisible() {
        if(mMovieDetailViewFrameLayout != null) {
            return mMovieDetailViewFrameLayout.getVisibility() != View.VISIBLE;
        }

        return false;
    }

}
