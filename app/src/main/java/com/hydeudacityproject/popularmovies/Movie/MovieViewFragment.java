package com.hydeudacityproject.popularmovies.Movie;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.hydeudacityproject.popularmovies.AppData;
import com.hydeudacityproject.popularmovies.Detail.MovieDetailView;
import com.hydeudacityproject.popularmovies.R;
import com.hydeudacityproject.popularmovies.Service.Framework.Movie;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieVideo;
import com.hydeudacityproject.popularmovies.Service.GetHighestRatedMovies;
import com.hydeudacityproject.popularmovies.Service.GetMovieDetails;
import com.hydeudacityproject.popularmovies.Service.GetMovieDetailsResponse;
import com.hydeudacityproject.popularmovies.Service.GetMoviesResponse;
import com.hydeudacityproject.popularmovies.Service.GetNowPlayingMovies;
import com.hydeudacityproject.popularmovies.Service.GetPopularMovies;
import com.hydeudacityproject.popularmovies.Service.GetUpcomingMovies;
import com.hydeudacityproject.popularmovies.Service.MovieService;
import com.lonewolfgames.framework.AbstractViewAdapter;
import com.lonewolfgames.framework.AbstractViewData;
import com.lonewolfgames.framework.AbstractViewHolder;
import com.lonewolfgames.framework.Cache.Images.ImageLoader;
import com.lonewolfgames.framework.MarginDecoration;
import com.lonewolfgames.framework.UI.ProgressView;
import com.lonewolfgames.framework.Utilities;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/17/2015.
 */
public class MovieViewFragment extends Fragment implements GetPopularMovies.GetMoviesListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = MovieViewFragment.class.getSimpleName();

    public static final String KEY_MOVIE_DETAIL = "movie_detail";

    public static final String KEY_MOVIE_POSTER_IMAGE_PATH = "movie_poster_image_path";
    public static final String KEY_MOVIE_BACKDROP_IMAGE_PATH = "movie_backdrop_image_path";
    public static final String KEY_ADAPTER_DATA = "adapter_data";
    public static final String KEY_RESULT_CHOICE = "result_choice";
    public static final String KEY_FILTER_CHOICE = "filter_choice";


    private static MovieViewFragment mInstance;

    private Toolbar mToolbar;
    private Spinner mToolbarSpinner;

    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mListView;
    private ViewAdapter mListAdapter;
    private GridLayoutManager mListLayoutManager;

    private LinearLayout mNoContentLayout;

    private LinearLayout mTryAgainLayout;
    private Button mTryAgainButton;

    private LinearLayout mNoFavoritesLayout;

    private ProgressView mProgressView;

    private int mCurrentPage = 1;
    private MovieService.ResultType mResultType = MovieService.ResultType.Popular;

    private boolean mLoading = false;
    private boolean mReload = false;


    public static MovieViewFragment newInstance() {
        MovieViewFragment fragment = new MovieViewFragment();

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_view_fragment, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if(mToolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(mToolbar);
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            activity.getSupportActionBar().setTitle("");
        }

        mToolbarSpinner = (Spinner) view.findViewById(R.id.spinner_nav);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mToolbar.getContext(),
                R.array.movie_views, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mToolbarSpinner.setAdapter(adapter);

        // Update the spinner based on previous saved data
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_FILTER_CHOICE)) {
            mResultType = MovieService.ResultType.values()[savedInstanceState.getInt(KEY_FILTER_CHOICE)];
            mToolbarSpinner.setSelection(savedInstanceState.getInt(KEY_FILTER_CHOICE), false);
        } else {
            mToolbarSpinner.setSelection(0, false);
        }

        mToolbarSpinner.setOnItemSelectedListener(this);

        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        mProgressView = (ProgressView) view.findViewById(R.id.progress_view);
        mListView = (RecyclerView) view.findViewById(R.id.recyclerView_movies);

        // Set the different grid layout on orientation change
        if(((MovieView) getActivity()).isDetailVisible() || ((MovieView) getActivity()).isTabletView()) {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                mListLayoutManager = new GridLayoutManager(getActivity(), 3);
            } else {
                mListLayoutManager = new GridLayoutManager(getActivity(), 4);
            }
        } else {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mListLayoutManager = new GridLayoutManager(getActivity(), 2);
            } else {
                mListLayoutManager = new GridLayoutManager(getActivity(), 3);
            }
        }

        mListView.addItemDecoration(new MarginDecoration(0));
        mListView.setLayoutManager(mListLayoutManager);

        // Update the adapter based on previous saved data
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ADAPTER_DATA)) {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(KEY_ADAPTER_DATA);
            mListAdapter = new ViewAdapter(movies);
            mListView.setAdapter(mListAdapter);
        } else {
            mListAdapter = new ViewAdapter(new ArrayList<Movie>());
            mListView.setAdapter(mListAdapter);
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_RESULT_CHOICE)) {
            mResultType = MovieService.ResultType.values()[savedInstanceState.getInt(KEY_RESULT_CHOICE)];
        }

        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalCount = mListLayoutManager.getItemCount();
                int firstVisibleItem = mListLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItem = mListLayoutManager.findLastVisibleItemPosition();
                int difference = lastVisibleItem - firstVisibleItem;

                // Scroll is at the bottom so get more results by increasing
                // the current page
                if(totalCount - lastVisibleItem < difference && !mLoading) {
                    mCurrentPage++;
                    retrieveResults();
                }
            }
        });

        mTryAgainLayout = (LinearLayout) view.findViewById(R.id.linearLayout_try_again);
        mTryAgainButton = (Button) view.findViewById(R.id.button_try_again);
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveResults();
            }
        });

        mNoFavoritesLayout = (LinearLayout) view.findViewById(R.id.linearLayout_no_favorites);

        // Initial Load
        if(mListAdapter != null && mListAdapter.size() == 0) {
            retrieveResults();
        }

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save choice
        outState.putInt(KEY_FILTER_CHOICE, mToolbarSpinner.getSelectedItemPosition());

        // Save user data to reload
        outState.putParcelableArrayList(KEY_ADAPTER_DATA, mListAdapter.data());
        outState.putInt(KEY_RESULT_CHOICE, mResultType.ordinal());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0:
                mResultType = MovieService.ResultType.Popular;
                break;
            case 1:
                mResultType = MovieService.ResultType.HighestRated;
                break;
            case 2:
                mResultType = MovieService.ResultType.NowPlaying;
                break;
            case 3:
                mResultType = MovieService.ResultType.Upcoming;
                break;
            case 4:
                mResultType = MovieService.ResultType.Favorites;
        }

        reload(mResultType);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void reloadFavorites() {
        if(mResultType == MovieService.ResultType.Favorites) {
            reload(MovieService.ResultType.Favorites);
        }
    }

    private void reload(MovieService.ResultType resultType) {
        Log.i(TAG, "reload");
        mCurrentPage = 1;
        mReload = true;
        retrieveResults(resultType);
    }

    private void retrieveResults() {
        retrieveResults(mResultType);
    }
    /**
     * Retrieves the movie results
     */
    private void retrieveResults(MovieService.ResultType resultType) {
        if(!mLoading) {
            Log.i(TAG, "retrieveResults page: " + mCurrentPage);
            mLoading = true;
            mResultType = resultType;
            hideTryAgain();
            hideNoFavoritesView();
            showProgressView();

            switch(resultType) {
                case Popular:
                    new GetPopularMovies(getActivity(), this, mCurrentPage).execute();
                    break;
                case HighestRated:
                    new GetHighestRatedMovies(getActivity(), this, mCurrentPage).execute();
                    break;
                case Upcoming:
                    new GetUpcomingMovies(getActivity(), this, mCurrentPage).execute();
                    break;
                case NowPlaying:
                    new GetNowPlayingMovies(getActivity(), this, mCurrentPage).execute();
                    break;
                case Favorites: {
                    ArrayList<Movie> favorites = AppData.getInstance().favoriteMovies();

                    mListAdapter = new ViewAdapter(favorites);
                    mListView.setAdapter(mListAdapter);
                    mListAdapter.notifyDataSetChanged();

                    if(favorites.size() == 0) {
                        showNoFavoritesView();
                    }
                    hideProgressView();
                    mLoading = false;
                }
                break;
            }
        }
    }


    /**
     * Hide the progress view
     */
    private void hideProgressView() {
        if(mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
        }
    }

    /**
     * Show the progress view
     */
    private void showProgressView() {
        if(mProgressView != null) {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hide the try again layout
     */
    private void hideTryAgain() {

        if(mTryAgainLayout != null) {
            mTryAgainLayout.setVisibility(View.GONE);
        }

        if(mListView != null) {
            mListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show the try again layout
     */
    private void showTryAgain() {

        if(mTryAgainLayout != null) {
            mTryAgainLayout.setVisibility(View.VISIBLE);
        }

        if(mListView != null) {
            mListView.setVisibility(View.GONE);
        }
    }

    /**
     * Hide the no favorites view
     */
    private void hideNoFavoritesView() {
        if(mNoFavoritesLayout != null) {
            mNoFavoritesLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Show the favorites view
     */
    private void showNoFavoritesView() {
        if(mNoFavoritesLayout != null) {
            mNoFavoritesLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnGetMoviesFinished(GetMoviesResponse response) {
        hideTryAgain();
        hideProgressView();
        mLoading = false;
        if(mListAdapter.size() > 0 && !mReload) {
            mListAdapter.addAll(response.items());
        } else {
            mListAdapter = new ViewAdapter(response.items());
            mListView.setAdapter(mListAdapter);
        }

        mReload = false;
    }

    @Override
    public void OnGetMoviesError(MovieService.ServiceError error) {
        if(error == MovieService.ServiceError.NetworkConnectionError) {
            showTryAgain();
            hideProgressView();
            mCurrentPage = 1;
            mLoading = false;

            showError();
        }
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, "No network connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        retrieveResults();
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }


    private static class ViewAdapter extends AbstractViewAdapter<ViewAdapter.Item, Movie, AbstractViewHolder> {

        public ViewAdapter(ArrayList<Movie> items) {
            super();

            addAll(items);
        }

        @Override
        public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder.Builder(parent, R.layout.movie_view_item).build();
        }

        @Override
        public void addAll(ArrayList<Movie> items) {
            // Add the content items
            for(Movie item : items) {
                addItem(new Item(item, 0));
            }

            if(items.size() == 0) {
                notifyDataSetChanged();
            }
        }


        public static class Item extends AbstractViewData<Movie> {
            public Item(Movie item, int type) {
                super(item, type);
            }
        }


        public static class ItemViewHolder extends AbstractViewHolder<Item, ViewAdapter> implements
                GetMovieDetails.GetMovieDetailsListener {

            private ImageView mMovieImageView;
            private ImageLoader mPosterImageLoader;
            private ImageLoader mBackdropImageLoader;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                mMovieImageView = (ImageView) itemView.findViewById(R.id.imageView_movie_thumbnail);

                if(mPosterImageLoader != null) {
                    if(mPosterImageLoader.getStatus() == AsyncTask.Status.RUNNING) {
                        mPosterImageLoader.cancel(true);
                        mPosterImageLoader = null;
                    }
                }

                if(mBackdropImageLoader != null) {
                    if(mBackdropImageLoader.getStatus() == AsyncTask.Status.RUNNING) {
                        mBackdropImageLoader.cancel(true);
                        mBackdropImageLoader = null;
                    }
                }
            }

            @Override
            public void OnGetMovieDetailsFinished(GetMovieDetailsResponse response) {
                mInstance.hideProgressView();
                MovieDetail movie = response.item();

                showMovieDetails(movie);
            }

            @Override
            public void OnGetMovieDetailsError(MovieService.ServiceError error) {
                mInstance.showError();
            }

            @Override
            public void initialize(final Item data, final int position) {
                mMovieImageView.setImageResource(R.drawable.img_missing_poster);

                if(!data.item().posterImage().contains("null")) {
                    mPosterImageLoader = new ImageLoader.Builder(itemView.getContext())
                            .imageView(mMovieImageView)
                            .url(data.item().posterImage())
                            .defaultResourceId(R.drawable.img_missing_poster)
                            .saveToDisk(true)
                            .filename(data.item().posterPath())
                            .build();
                    mPosterImageLoader.execute();
                }

                if(!data.item().backdropImage().contains("null")) {

                    mBackdropImageLoader = new ImageLoader.Builder(itemView.getContext())
                            .url(data.item().backdropImage())
                            .saveToDisk(true)
                            .filename(data.item().backdropPath())
                            .build();
                    mBackdropImageLoader.execute();
                }


                mMovieImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MovieDetail movie = AppData.getInstance().getFavorite(data.item().id());

                        if(movie != null) {
                            showMovieDetails(movie);
                            return;
                        }

                        if(!Utilities.isNetworkAvailable(v.getContext())) {
                            mInstance.showError();
                            return;
                        } else {
                            mInstance.showProgressView();
                            new GetMovieDetails(v.getContext(), ItemViewHolder.this, data.item().id()).execute();
                        }
                    }
                });
            }

            private void showMovieDetails(MovieDetail movie) {
                ((MovieSelectedListener) mInstance.getActivity()).OnMovieSelected(itemView, movie);
            }

            public static class Builder extends AbstractViewHolder.Builder {

                public Builder(ViewGroup parent, int layoutResourceId) {
                    super(parent, layoutResourceId);
                }

                @Override
                public ItemViewHolder build() {
                    View view = LayoutInflater.from(mParent.getContext()).inflate(mLayoutResourceId, mParent, false);

                    return new ItemViewHolder(view);
                }
            }
        }
    }

    public interface MovieSelectedListener {
        void OnMovieSelected(View itemView, MovieDetail movie);
    }
}
