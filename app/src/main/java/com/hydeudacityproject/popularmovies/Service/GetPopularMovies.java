package com.hydeudacityproject.popularmovies.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.hydeudacityproject.popularmovies.AppSettings;
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
public class GetPopularMovies extends AsyncTask<Void, Void, GetMoviesResponse> {

    private Context mContext;
    private GetMoviesListener mListener;
    private String mUrl = MovieService.MOVIE_API_BASE_URL +
                                MovieService.MOVIE_API_MOVIE +
                                MovieService.MOVIE_API_POPULAR +
                                MovieService.MOVIE_API_URL_START +
                                MovieService.MOVIE_ADI_URL_SEPARATOR +
                                MovieService.MOVIE_API_KEY;

    private int MAX_PAGE = 1000;

    private ServiceError mErrorCode;

    public GetPopularMovies(Context context, GetMoviesListener listener, int page) {

        mContext = context;

        if(page > MAX_PAGE) {
            return;
        }

        mListener = listener;
        mUrl += MovieService.MOVIE_ADI_URL_SEPARATOR +
                MovieService.MOVIE_API_PAGE +
                String.valueOf(page);
    }

    @Override
    protected GetMoviesResponse doInBackground(Void... params) {

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

                return new GetMoviesResponse.Builder().fromJSON(content.toString()).build();

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
    protected void onPostExecute(GetMoviesResponse result) {
        super.onPostExecute(result);

        if(mListener != null) {
            if(mErrorCode == ServiceError.Success) {
                mListener.OnGetMoviesFinished(result);
            } else {
                mListener.OnGetMoviesError(mErrorCode);
            }
        }
    }

    public interface GetMoviesListener {
        void OnGetMoviesFinished(GetMoviesResponse response);
        void OnGetMoviesError(ServiceError error);
    }


}
