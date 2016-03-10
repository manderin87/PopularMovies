package com.hydeudacityproject.popularmovies.Detail.Reviews;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.hydeudacityproject.popularmovies.Movie.MovieView;
import com.hydeudacityproject.popularmovies.Movie.MovieViewFragment;
import com.hydeudacityproject.popularmovies.R;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieReview;
import com.lonewolfgames.framework.AbstractViewAdapter;
import com.lonewolfgames.framework.AbstractViewData;
import com.lonewolfgames.framework.AbstractViewHolder;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/17/2015.
 */
public class ReviewsDialog extends DialogFragment {

    private MovieDetail mMovieDetail;

    private CoordinatorLayout mCoordinatorLayout;
    private RecyclerView mListView;
    private ViewAdapter mListAdapter;

    private LinearLayoutManager mListLayoutManager;

    public static final ReviewsDialog newInstance(MovieDetail movieDetail) {
        ReviewsDialog fragment = new ReviewsDialog();

        fragment.setMovieDetail(movieDetail);
        return fragment;
    }

    private void setMovieDetail(MovieDetail movieDetail) {
        mMovieDetail = movieDetail;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_detail_review_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey(MovieViewFragment.KEY_MOVIE_DETAIL)) {
            mMovieDetail = savedInstanceState.getParcelable(MovieViewFragment.KEY_MOVIE_DETAIL);
        }

        mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        mListView = (RecyclerView) view.findViewById(R.id.recyclerView_reviews);
        mListLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mListLayoutManager);

        mListAdapter = new ViewAdapter(new ArrayList<MovieReview>());
        mListView.setAdapter(mListAdapter);

        initializeMovieReviews(mMovieDetail.reviewsList());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void initializeMovieReviews(ArrayList<MovieReview> reviews) {
        mListAdapter = new ViewAdapter(reviews);
        mListView.setAdapter(mListAdapter);
    }

    public static class ViewAdapter extends AbstractViewAdapter<ViewAdapter.Item, MovieReview, AbstractViewHolder> {

        public ViewAdapter(ArrayList<MovieReview> items) {
            super();

            addAll(items);
        }

        @Override
        public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder.Builder(parent, R.layout.movie_detail_review_dialog_item).build();
        }

        @Override
        public void addAll(ArrayList<MovieReview> items) {
            // Add the content items
            for(MovieReview item : items) {
                addItem(new Item(item, 0));
            }
        }

        public static class Item extends AbstractViewData<MovieReview> {
            public Item(MovieReview item, int type) {
                super(item, type);
            }
        }


        public static class ItemViewHolder extends AbstractViewHolder<Item, ViewAdapter> {

            private TextView mAuthorTextView;
            private TextView mContentTextView;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                mAuthorTextView = (TextView) itemView.findViewById(R.id.textView_author);
                mContentTextView = (TextView) itemView.findViewById(R.id.textView_content);
            }

            @Override
            public void initialize(final Item data, final int position) {
                mAuthorTextView.setText(data.item().author());
                mContentTextView.setText(data.item().content());
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
}
