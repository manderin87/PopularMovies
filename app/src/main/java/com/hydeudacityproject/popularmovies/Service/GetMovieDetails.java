package com.hydeudacityproject.popularmovies.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;

import com.hydeudacityproject.popularmovies.AppSettings;
import com.lonewolfgames.framework.AbstractAppService;
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
public class GetMovieDetails extends AsyncTask<Void, Void, GetMovieDetailsResponse> {

    private static final String TAG = GetMovieDetails.class.getSimpleName();

    private Context mContext;
    private GetMovieDetailsListener mListener;
    private String mUrl = MovieService.MOVIE_API_BASE_URL +
                            MovieService.MOVIE_API_MOVIE_DETAIL;

    private ServiceError mErrorCode;

    public GetMovieDetails(GetMovieDetailsListener listener, int id) {
        mContext = (Context) listener;

        mListener = listener;

        mUrl += String.valueOf(id) + MovieService.MOVIE_API_URL_START + MovieService.MOVIE_API_KEY;
    }

    public GetMovieDetails(Context context, GetMovieDetailsListener listener, int id) {
        mContext = context;

        mListener = listener;

        mUrl += String.valueOf(id) + MovieService.MOVIE_API_URL_START + MovieService.MOVIE_API_KEY;
    }

    @Override
    protected GetMovieDetailsResponse doInBackground(Void... params) {
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

                mErrorCode = MovieService.ServiceError.Success;

                return new GetMovieDetailsResponse.Builder().fromJSON(content.toString()).build();

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
            mErrorCode = MovieService.ServiceError.NetworkConnectionError;
        }

        return null;
    }

    @Override
    protected void onPostExecute(GetMovieDetailsResponse result) {
        super.onPostExecute(result);

        if(mListener != null) {
            if(mErrorCode == MovieService.ServiceError.Success) {
                mListener.OnGetMovieDetailsFinished(result);
            } else {
                mListener.OnGetMovieDetailsError(mErrorCode);
            }
        }
    }

    public interface GetMovieDetailsListener {
        void OnGetMovieDetailsFinished(GetMovieDetailsResponse response);
        void OnGetMovieDetailsError(MovieService.ServiceError error);
    }
}
