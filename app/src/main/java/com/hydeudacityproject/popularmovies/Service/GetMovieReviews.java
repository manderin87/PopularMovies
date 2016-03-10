package com.hydeudacityproject.popularmovies.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.lonewolfgames.framework.AbstractAppService.ServiceError;
import com.lonewolfgames.framework.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jhyde on 10/28/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class GetMovieReviews extends AsyncTask<Void, Void, GetMovieReviewsResponse> {

    private static final String TAG = GetMovieReviews.class.getSimpleName();

    private Context mContext;
    private GetMovieReviewsListener mListener;
    private String mUrl = MovieService.MOVIE_API_BASE_URL +
                            MovieService.MOVIE_API_MOVIE_DETAIL;

    private ServiceError mErrorCode;

    public GetMovieReviews(Context context, GetMovieReviewsListener listener, int id) {
        mContext = context;

        mListener = listener;

        mUrl += String.valueOf(id) + MovieService.MOVIE_API_MOVIE_REVIEWS +
                MovieService.MOVIE_API_URL_START + MovieService.MOVIE_API_KEY;
    }

    @Override
    protected GetMovieReviewsResponse doInBackground(Void... params) {
        if(Utilities.isNetworkAvailable(mContext)) {
            StringBuilder content = new StringBuilder();

            URL url;
            BufferedReader buffered_reader = null;

            try {
                url = new URL(mUrl);
                URLConnection connection = url.openConnection();
                buffered_reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String read_line;

                while((read_line = buffered_reader.readLine()) != null) {
                    content.append(read_line);
                }

                mErrorCode = ServiceError.Success;

                return new GetMovieReviewsResponse.Builder().fromJSON(content.toString()).build();

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(buffered_reader != null) {
                    try {
                        buffered_reader.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            mErrorCode = ServiceError.NetworkConnectionError;
        }

        return null;
    }

    @Override
    protected void onPostExecute(GetMovieReviewsResponse result) {
        super.onPostExecute(result);

        if(mListener != null) {
            if(mErrorCode == ServiceError.Success) {
                mListener.OnGetMovieReviewsFinished(result);
            } else {
                mListener.OnGetMovieReviewsError(mErrorCode);
            }
        }
    }

    public interface GetMovieReviewsListener {
        void OnGetMovieReviewsFinished(GetMovieReviewsResponse response);
        void OnGetMovieReviewsError(ServiceError error);
    }
}
