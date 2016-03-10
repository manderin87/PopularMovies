package com.hydeudacityproject.popularmovies.Service;

import com.hydeudacityproject.popularmovies.Service.Framework.MovieDetail;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class GetMovieVideosResponse {

    private ArrayList<MovieVideo> mItems;

    public ArrayList<MovieVideo> items() { return mItems; }

    public GetMovieVideosResponse(Builder build) {
        mItems = build.mItems;
    }

    public static class Builder {
        private ArrayList<MovieVideo> mItems;

        public Builder() { mItems = new ArrayList<>(); }

        public Builder fromJSON(String content) {
            int id = 0;

            try {
                JSONObject object = new JSONObject(content);

                id = object.getInt("id");

                JSONArray results_array = object.getJSONArray("results");

                for(int index = 0; index < results_array.length(); index++) {
                    mItems.add(new MovieVideo.Builder().fromJSON(results_array.getString(index)).build());
                }

            } catch(JSONException e) {

            }

            return this;
        }

        public GetMovieVideosResponse build() {
            return new GetMovieVideosResponse(this);
        }
    }
}
