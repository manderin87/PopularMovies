package com.hydeudacityproject.popularmovies.Service;

import com.hydeudacityproject.popularmovies.Service.Framework.Movie;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class GetMovieDetailsResponse {

    private MovieDetail mItem;

    public MovieDetail item() { return mItem; }

    public GetMovieDetailsResponse(Builder build) {
        mItem = build.mItem;
    }

    public static class Builder {
        private MovieDetail mItem;

        public Builder() { }

        public Builder fromJSON(String content) {
            mItem = new MovieDetail.Builder().fromJSON(content).build();

            return this;
        }

        public GetMovieDetailsResponse build() {
            return new GetMovieDetailsResponse(this);
        }
    }
}
