package com.hydeudacityproject.popularmovies.Detail;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.hydeudacityproject.popularmovies.AppData;
import com.hydeudacityproject.popularmovies.Detail.Poster.PosterDialog;
import com.hydeudacityproject.popularmovies.Detail.Reviews.ReviewsDialog;
import com.hydeudacityproject.popularmovies.Movie.MovieView;
import com.hydeudacityproject.popularmovies.R;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieVideo;
import com.hydeudacityproject.popularmovies.Service.GetMovieReviews;
import com.hydeudacityproject.popularmovies.Service.GetMovieReviewsResponse;
import com.hydeudacityproject.popularmovies.Service.GetMovieVideos;
import com.hydeudacityproject.popularmovies.Service.GetMovieVideosResponse;
import com.hydeudacityproject.popularmovies.Service.MovieService;
import com.lonewolfgames.framework.AbstractAppService;
import com.lonewolfgames.framework.Cache.Images.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class MovieDetailViewFragment extends Fragment implements
        GetMovieReviews.GetMovieReviewsListener, GetMovieVideos.GetMovieVideosListener {

    private static final String TAG = MovieDetailViewFragment.class.getSimpleName();

    private static final String KEY_MOVIE_DETAIL = "movie_detail";

    private MovieDetail mMovieDetail = null;

    private Toolbar mToolbar;

    private FrameLayout mFrameLayout;
    private NestedScrollView mScrollViewLayout;
    private TextView mTitleTitleTextView;
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private LinearLayout mInfoLayout;
    private TextView mReleaseDateTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mRuntimeTitleTextView;
    private TextView mRuntimeTextView;
    private TextView mUserRatingTitleTextView;
    private TextView mUserRatingTextView;

    private LinearLayout mExtrasLayout;

    private TextView mOverviewTextView;

    private boolean mVideosLayoutLoaded = false;
    private LinearLayout mVideosLayout;
    private TextView mVideosTitleTextView;
    private LinearLayout mVideoItemsLayout;

    private Palette.Swatch mLightVibrantSwatch;
    private Palette.Swatch mVibrantSwatch;
    private Palette.Swatch mDarkVibrantSwatch;

    private ReviewsDialog mReviewsDialog;

    private CoordinatorLayout mCoordinatorLayout;
    private CollapsingToolbarLayout mCollapsingLayout;
    private TextView mSubTitleTextView;
    private ImageView mBackdropImageView;

    private Bitmap mPosterImage = null;
    private Bitmap mBackdropImage = null;

    private ImageButton mFavoriteImageButton;
    private ImageButton mReviewsImageButton;
    private ImageButton mShareImageButton;

    private HashMap<String, YouTubeThumbnailLoader> mYouTubeLoader = new HashMap<>();

    public static MovieDetailViewFragment newInstance(MovieDetail movieDetail) {
        MovieDetailViewFragment fragment = new MovieDetailViewFragment();
        fragment.setMovieDetail(movieDetail);

        return fragment;
    }

    private void setMovieDetail(MovieDetail movieDetail) { mMovieDetail = movieDetail; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        if(mToolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(mToolbar);

            if(!(getActivity() instanceof MovieView) || !((MovieView) getActivity()).isTabletView()) {
                activity.getSupportActionBar().setHomeButtonEnabled(true);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            activity.getSupportActionBar().setTitle("");
        }

        mCollapsingLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingLayout);
        mSubTitleTextView = (TextView) view.findViewById(R.id.textView_subtitle);
        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        mBackdropImageView = (ImageView) view.findViewById(R.id.imageView_movie_backdrop);

        mFrameLayout = (FrameLayout) view.findViewById(R.id.frameLayout_details);
        mScrollViewLayout = (NestedScrollView) view.findViewById(R.id.nestedScrollView_details);
        mTitleTitleTextView = (TextView) view.findViewById(R.id.textView_title_title);
        mTitleTextView = (TextView) view.findViewById(R.id.textView_title);



        mPosterImageView = (ImageView) view.findViewById(R.id.imageView_movie_poster);
        mPosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PosterDialog dialog = PosterDialog.newInstance(mPosterImage);
                dialog.show(getFragmentManager(), "Poster Dialog");
            }
        });

        mInfoLayout = (LinearLayout) view.findViewById(R.id.linearLayout_info);
        mReleaseDateTitleTextView = (TextView) view.findViewById(R.id.textView_release_date_title);
        mReleaseDateTextView = (TextView) view.findViewById(R.id.textView_release_date);
        mRuntimeTitleTextView = (TextView) view.findViewById(R.id.textView_runtime_title);
        mRuntimeTextView = (TextView) view.findViewById(R.id.textView_runtime);
        mUserRatingTitleTextView = (TextView) view.findViewById(R.id.textView_user_rating_title);
        mUserRatingTextView = (TextView) view.findViewById(R.id.textView_user_rating);
        mExtrasLayout = (LinearLayout) view.findViewById(R.id.linearLayout_extras);
        mOverviewTextView = (TextView) view.findViewById(R.id.textView_plot);
        mVideosLayout = (LinearLayout) view.findViewById(R.id.linearLayout_videos);
        mVideosTitleTextView = (TextView) view.findViewById(R.id.textView_videos_title);
        mVideoItemsLayout = (LinearLayout) view.findViewById(R.id.linearLayout_video_items);

        mFavoriteImageButton = (ImageButton) view.findViewById(R.id.imageButton_favorite);
        mFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        mReviewsImageButton = (ImageButton) view.findViewById(R.id.imageButton_reviews);
        mReviewsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayReviews();
            }
        });

        mShareImageButton = (ImageButton) view.findViewById(R.id.imageButton_share);
        mShareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMovieDetail.hasVideos()) {
                    MovieVideo video = mMovieDetail.videosList().get(0);
//                    Intent share = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.key()));
//                    startActivity(share);
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, video.name());
                    share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + video.key());
                    startActivity(Intent.createChooser(share, "Share " + mMovieDetail.title() + ": " + video.name()));

                }
            }
        });


        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(KEY_MOVIE_DETAIL)) {
                mMovieDetail = savedInstanceState.getParcelable(KEY_MOVIE_DETAIL);
            }
        }

        if(mCollapsingLayout != null) {
            mCollapsingLayout.setTitle("");
            mCollapsingLayout.setContentScrimColor(Color.parseColor("#7D000000"));
            mCollapsingLayout.setTitle(mMovieDetail.title());
            mSubTitleTextView.setText(mMovieDetail.genres());
        }

        if(mPosterImage == null) {
            ImageLoader poster_image_loader = new ImageLoader.Builder(getActivity())
                    .imageView(mPosterImageView)
                    .url(mMovieDetail.posterImage())
                    .filename(mMovieDetail.posterPath())
                    .defaultResourceId(R.drawable.img_missing_poster)
                    .listener(new ImageLoader.ImageLoaderListener() {
                        @Override
                        public void OnImageLoaded(Bitmap bitmap) {

                            mPosterImage = bitmap;

                            updateViewColors();
                        }

                        @Override
                        public void OnImageFailed(AbstractAppService.ServiceError error) { }
                    })
                    .build();
            poster_image_loader.execute();
        } else {
            mPosterImageView.setImageBitmap(mPosterImage);
        }

        if(mBackdropImage == null) {
            ImageLoader backdrop_image_loader = new ImageLoader.Builder(getActivity())
                    .imageView(mBackdropImageView)
                    .url(mMovieDetail.backdropImage())
                    .filename(mMovieDetail.backdropPath())
                    .defaultResourceId(R.drawable.img_missing_backdrop)
                    .listener(new ImageLoader.ImageLoaderListener() {
                        @Override
                        public void OnImageLoaded(Bitmap bitmap) {
                            mBackdropImage = bitmap;
                        }

                        @Override
                        public void OnImageFailed(AbstractAppService.ServiceError error) { }
                    })
                    .build();
            backdrop_image_loader.execute();
        } else {
            mBackdropImageView.setImageBitmap(mBackdropImage);
        }

        initializeMovieDetails(mMovieDetail);

        if(!mMovieDetail.isVideosLoaded()) {
            new GetMovieVideos(getActivity(), MovieDetailViewFragment.this, mMovieDetail.id()).execute();
        } else {
            initializeMovieVideos(mMovieDetail.videosList());
        }

        if(!mMovieDetail.isReviewsLoaded()) {
            new GetMovieReviews(getActivity(), MovieDetailViewFragment.this, mMovieDetail.id()).execute();
        }

        updateFavoritesButton();
        updateShareButton(mMovieDetail.hasVideos());
        updateReviewsButton(mMovieDetail.hasReviews());

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        for(YouTubeThumbnailLoader loader : mYouTubeLoader.values()) {
            loader.release();
        }
    }

    /**
     * Update the view colors
     */
    private void updateViewColors() {
        if(mPosterImage != null) {
            Palette.from(mPosterImage).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {

                    mLightVibrantSwatch = palette.getLightVibrantSwatch();
                    mVibrantSwatch = palette.getVibrantSwatch();
                    mDarkVibrantSwatch = palette.getDarkVibrantSwatch();


                    if(mLightVibrantSwatch != null) {
                        mFrameLayout.setBackgroundColor(mLightVibrantSwatch.getRgb());
                        mTitleTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mTitleTitleTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mReleaseDateTitleTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mReleaseDateTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mRuntimeTitleTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mRuntimeTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mUserRatingTitleTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mUserRatingTextView.setTextColor(mLightVibrantSwatch.getTitleTextColor());
                        mInfoLayout.setBackgroundColor(mLightVibrantSwatch.getBodyTextColor());
                    }

                    if(mVibrantSwatch != null) {

                        mTitleTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mTitleTitleTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mReleaseDateTitleTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mReleaseDateTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mRuntimeTitleTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mRuntimeTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mUserRatingTitleTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mUserRatingTextView.setTextColor(mVibrantSwatch.getTitleTextColor());
                        mInfoLayout.setBackgroundColor(mVibrantSwatch.getRgb());

                    }

                    if(mDarkVibrantSwatch != null) {
                        mOverviewTextView.setTextColor(mDarkVibrantSwatch.getTitleTextColor());
                        mExtrasLayout.setBackgroundColor(mDarkVibrantSwatch.getRgb());
                        mVideosTitleTextView.setTextColor(mDarkVibrantSwatch.getTitleTextColor());
                        mScrollViewLayout.setBackgroundColor(mDarkVibrantSwatch.getRgb());
                        mCoordinatorLayout.setBackgroundColor(mDarkVibrantSwatch.getRgb());
                        mFrameLayout.setBackgroundColor(mDarkVibrantSwatch.getRgb());

                        if(mMovieDetail.videosList().size() > 0 && mVideoItemsLayout.getChildCount() > 0) {
                            for(int index = 0; index < mVideoItemsLayout.getChildCount(); index++) {
                                View view = mVideoItemsLayout.getChildAt(index);

                                TextView name_text_view = (TextView) view.findViewById(R.id.textView_name);
                                name_text_view.setTextColor(mDarkVibrantSwatch.getTitleTextColor());

//                                Drawable icon = ContextCompat.getDrawable(MovieDetailView.this, R.drawable.icn_video);
//                                icon.setColorFilter(mDarkVibrantSwatch.getTitleTextColor(), PorterDuff.Mode.DST_OVER);
//                                ImageView icon_image_view = (ImageView) view.findViewById(R.id.imageView_icon);
//                                icon_image_view.setImageDrawable(icon);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Add the movie to favorites
     */
    private void addToFavorites() {
        if(mMovieDetail != null) {
            AppData.getInstance().addToFavorites(mMovieDetail);
        }
    }

    /**
     * Remove the movie from favorites
     */
    private void removeFromFavorites() {
        if(mMovieDetail != null) {
            AppData.getInstance().removeFromFavorites(mMovieDetail.id());
        }
    }

    /**
     * Display the reviews dialog view
     */
    private void displayReviews() {
        if(mReviewsDialog == null) {
            mReviewsDialog = ReviewsDialog.newInstance(mMovieDetail);
        }

        mReviewsDialog.show(getFragmentManager(), "Review Dialog");
    }

    /**
     * Initialize the movie details view
     * @param movie
     */
    private void initializeMovieDetails(MovieDetail movie) {
        if(movie != null) {
            mTitleTextView.setText(movie.title() + " (" + MovieService.RELEASE_DATE_YEAR_FORMAT.format(movie.releaseDateAsDate()) + ")");
            mReleaseDateTextView.setText(MovieService.RELEASE_DATE_FORMAT.format(movie.releaseDateAsDate()));
            mRuntimeTextView.setText(String.valueOf(movie.runtime()) + " minutes");
            mUserRatingTextView.setText(String.valueOf(movie.voteAverage()) + " (" + String.valueOf(movie.voteCount()) + ")");
            mOverviewTextView.setText(movie.overview());

            updateViewColors();
        }
    }

    /**
     * Initialize the movie videos view
     * @param videos the videos list
     */
    private void initializeMovieVideos(ArrayList<MovieVideo> videos) {
        if(mVideosLayoutLoaded) {
            return;
        }

        if(videos != null && videos.size() > 0) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mVideosLayout.setVisibility(View.VISIBLE);
            for(int index = 0; index < videos.size(); index++) {
                final MovieVideo video = videos.get(index);
                View view = inflater.inflate(R.layout.movie_detail_video_item, mVideoItemsLayout, false);

                if(video.site().equalsIgnoreCase("YouTube")) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                                    MovieService.YOUTUBE_API_KEY, video.key()));
                        }
                    });
                }

                YouTubeThumbnailView thumbnail_view = (YouTubeThumbnailView) view.findViewById(R.id.imageView_thumbnail);
                thumbnail_view.initialize(MovieService.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        if(!mYouTubeLoader.containsKey(video.key())) {
                            youTubeThumbnailLoader.setVideo(video.key());

                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                                @Override public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {}
                                @Override public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {}
                            });

                            mYouTubeLoader.put(video.key(), youTubeThumbnailLoader);
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

                TextView name_text_view = (TextView) view.findViewById(R.id.textView_name);
                name_text_view.setText(video.name());
                name_text_view.setTextColor(Color.BLACK);

                mVideoItemsLayout.addView(view);
            }
        } else {
            mVideosLayout.setVisibility(View.GONE);
        }

        updateViewColors();

        mVideosLayoutLoaded = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_MOVIE_DETAIL, mMovieDetail);
    }

    @Override
    public void OnGetMovieVideosFinished(GetMovieVideosResponse response) {
        if(response != null) {
            mMovieDetail.setVideosList(response.items());
            initializeMovieVideos(mMovieDetail.videosList());

            updateShareButton(mMovieDetail.hasVideos());
        }
    }

    @Override
    public void OnGetMovieVideosError(AbstractAppService.ServiceError error) {
        if(error == MovieService.ServiceError.NetworkConnectionError) {
            Snackbar snackbar = Snackbar
                    .make(mScrollViewLayout, "No network connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetMovieVideos(getActivity(), MovieDetailViewFragment.this, mMovieDetail.id()).execute();
                        }
                    });

            snackbar.setActionTextColor(Color.RED);

            View snack_bar_view = snackbar.getView();
            TextView textView = (TextView) snack_bar_view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void OnGetMovieReviewsFinished(GetMovieReviewsResponse response) {
        if(response != null) {
            mMovieDetail.setReviewsList(response.items());

            updateReviewsButton(mMovieDetail.hasReviews());
        }
    }

    @Override
    public void OnGetMovieReviewsError(AbstractAppService.ServiceError error) {
        if(error == MovieService.ServiceError.NetworkConnectionError) {
            Snackbar snackbar = Snackbar
                    .make(mScrollViewLayout, "No network connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new GetMovieReviews(getActivity(), MovieDetailViewFragment.this, mMovieDetail.id()).execute();
                        }
                    });

            snackbar.setActionTextColor(Color.RED);

            View snack_bar_view = snackbar.getView();
            TextView textView = (TextView) snack_bar_view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    /**
     * Updates the reviews button
     * @param hasReviews
     */
    private void updateReviewsButton(boolean hasReviews) {
        if(mReviewsImageButton != null) {
            if(hasReviews) {
                mReviewsImageButton.setVisibility(View.VISIBLE);
            } else {
                mReviewsImageButton.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Updates the favorites button
     */
    private void updateFavoritesButton() {
        if(mFavoriteImageButton != null) {
            if(AppData.getInstance().isFavorite(mMovieDetail.id())) {
                mFavoriteImageButton.setImageResource(R.drawable.icn_favorite);
            } else {
                mFavoriteImageButton.setImageResource(R.drawable.icn_not_favorite);
            }

            if(getActivity() instanceof MovieView) {
                ((MovieView) getActivity()).reloadFavorites();
            }
        }
    }

    /**
     * Updates the share button
     * @param hasVideos
     */
    private void updateShareButton(boolean hasVideos) {
        if(mShareImageButton != null) {
            if(hasVideos) {
                mShareImageButton.setVisibility(View.VISIBLE);
            } else {
                mShareImageButton.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Toggles the movie favoriting
     */
    private void toggleFavorite() {
        if(AppData.getInstance().isFavorite(mMovieDetail.id())) {
            removeFromFavorites();
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, mMovieDetail.title() + " removed from favorites!", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addToFavorites();
                            updateFavoritesButton();
                        }
                    });

            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } else {
            addToFavorites();
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, mMovieDetail.title() + " added to favorites!", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            removeFromFavorites();
                            updateFavoritesButton();
                        }
                    });

            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
        updateFavoritesButton();
    }
}
