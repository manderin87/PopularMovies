package com.hydeudacityproject.popularmovies.Service.Framework;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jhyde on 11/10/2015.
 */
public class MovieReview implements Parcelable {

    private static final String DATA_SEPARATOR = "$";

    private String mId = "";
    private String mAuthor = "";
    private String mContent = "";
    private String mUrl = "";

    public String author() { return mAuthor; }
    public String content() { return mContent; }


    public String asString() {
        return mId + DATA_SEPARATOR + mAuthor + DATA_SEPARATOR +
                mContent + DATA_SEPARATOR + mUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mUrl);
    }

    private MovieReview(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        mUrl = in.readString();
    }

    public static final Parcelable.Creator<MovieReview> CREATOR
            = new Parcelable.Creator<MovieReview>() {

        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    private MovieReview(Builder build) {
        mId = build.mId;
        mAuthor = build.mAuthor;
        mContent = build.mContent;
        mUrl = build.mUrl;
    }

    public static class Builder {
        private String mId = "";
        private String mAuthor = "";
        private String mContent = "";
        private String mUrl = "";

        public Builder() {}

        public Builder fromJSON(String content) {
            try {
                JSONObject json_object = new JSONObject(content);

                mId = json_object.getString("id");
                mAuthor = json_object.getString("author");
                mContent = json_object.getString("content");
                mUrl = json_object.getString("url");
            } catch (JSONException e) {

            }

            return this;
        }

        public Builder fromDatabase(String content) {

            String[] values = content.split("\\" + DATA_SEPARATOR);

            mId = values[0];
            mAuthor = values[1];
            mContent = values[2];
            mUrl = values[3];

            return this;
        }

        public MovieReview build() {
            return new MovieReview(this);
        }
    }
}
