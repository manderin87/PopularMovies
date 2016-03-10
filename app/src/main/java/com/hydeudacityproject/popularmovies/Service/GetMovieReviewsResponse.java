package com.hydeudacityproject.popularmovies.Service;

import com.hydeudacityproject.popularmovies.Service.Framework.MovieReview;
import com.hydeudacityproject.popularmovies.Service.Framework.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class GetMovieReviewsResponse {

    private int mId = 0;
    private int mTotalPages = 0;
    private ArrayList<MovieReview> mItems;

    public ArrayList<MovieReview> items() { return mItems; }

    public GetMovieReviewsResponse(Builder build) {
        mId = build.mId;
        mTotalPages = build.mTotalPages;
        mItems = build.mItems;
    }

    public static class Builder {
        private int mId = 0;
        private int mTotalPages = 0;
        private ArrayList<MovieReview> mItems;

        public Builder() { mItems = new ArrayList<>(); }

        public Builder fromJSON(String content) {

            try {
                JSONObject object = new JSONObject(content);

                mId = object.getInt("id");
                mTotalPages = object.getInt("total_pages");

                JSONArray results_array = object.getJSONArray("results");

                for(int index = 0; index < results_array.length(); index++) {
                    mItems.add(new MovieReview.Builder().fromJSON(results_array.getString(index)).build());
                }

            } catch(JSONException e) {

            }

            return this;
        }

        public GetMovieReviewsResponse build() {
            return new GetMovieReviewsResponse(this);
        }
    }
}
