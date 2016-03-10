package com.hydeudacityproject.popularmovies.Service;

import com.hydeudacityproject.popularmovies.Service.Framework.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class GetMoviesResponse {

    private ArrayList<Movie> mItems;

    public ArrayList<Movie> items() { return mItems; }

    public GetMoviesResponse(Builder build) {
        mItems = build.mItems;
    }

    public static class Builder {
        private ArrayList<Movie> mItems;

        public Builder() {
            mItems = new ArrayList<>();
        }

        public Builder fromJSON(String content) {
            try {
                JSONObject json_object = new JSONObject(content);

                JSONArray results_array = json_object.getJSONArray("results");

                for(int index = 0; index < results_array.length(); index++) {
                    mItems.add(new Movie.Builder().fromJSON(results_array.getString(index)).build());
                }

            } catch(Exception e) {

            }

            return this;
        }

        public GetMoviesResponse build() {
            return new GetMoviesResponse(this);
        }
    }
}
