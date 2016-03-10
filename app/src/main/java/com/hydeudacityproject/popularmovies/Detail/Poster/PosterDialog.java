package com.hydeudacityproject.popularmovies.Detail.Poster;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

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
public class PosterDialog extends DialogFragment {

    private Bitmap mPosterBitmap;
    private ImageView mPosterImageView;

    public static final PosterDialog newInstance(Bitmap posterBitmap) {
        PosterDialog fragment = new PosterDialog();

        fragment.setPosterBitmap(posterBitmap);
        return fragment;
    }

    private void setPosterBitmap(Bitmap posterBitmap) {
        mPosterBitmap = posterBitmap;
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
        return inflater.inflate(R.layout.movie_detail_poster_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPosterImageView = (ImageView) view.findViewById(R.id.imageView_movie_poster);
        mPosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PosterDialog.this.dismiss();
            }
        });
        if(mPosterBitmap != null) {
            mPosterImageView.setImageBitmap(mPosterBitmap);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
