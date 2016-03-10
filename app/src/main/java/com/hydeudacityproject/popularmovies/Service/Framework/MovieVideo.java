package com.hydeudacityproject.popularmovies.Service.Framework;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonewolfgames.framework.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jhyde on 11/10/2015.
 */
public class MovieVideo implements Parcelable {

    private static final String DATA_SEPARATOR = "$";

    private String mId = "";
    private String mISO_639_1 = "";
    private String mKey = "";
    private String mName = "";
    private String mSite = "";
    private int mSize = 0;
    private String mType = "";

    public String name() { return mName; }
    public String key() { return mKey; }
    public String site() { return mSite; }
    public String type() { return mType; }



    public String asString() {
        return mId + DATA_SEPARATOR + mISO_639_1 + DATA_SEPARATOR + mKey + DATA_SEPARATOR + mName + DATA_SEPARATOR +
                mSite + DATA_SEPARATOR + String.valueOf(mSize) + DATA_SEPARATOR + mType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mISO_639_1);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeInt(mSize);
        dest.writeString(mType);
    }

    private MovieVideo(Parcel in) {
        mId = in.readString();
        mISO_639_1 = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mSize = in.readInt();
        mType = in.readString();
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR
            = new Parcelable.Creator<MovieVideo>() {

        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    private MovieVideo(Builder build) {
        mId = build.mId;
        mISO_639_1 = build.mISO_639_1;
        mKey = build.mKey;
        mName = build.mName;
        mSite = build.mSite;
        mSize = build.mSize;
        mType = build.mType;
    }

    public static class Builder {
        private int mMovieId = 0;
        private String mId = "";
        private String mISO_639_1 = "";
        private String mKey = "";
        private String mName = "";
        private String mSite = "";
        private int mSize = 0;
        private String mType = "";

        public Builder() {}

        public Builder fromJSON(String content) {
            try {
                JSONObject json_object = new JSONObject(content);

                mId = json_object.getString("id");
                mISO_639_1 = json_object.getString("iso_639_1");
                mKey = json_object.getString("key");
                mName = json_object.getString("name");
                mSite = json_object.getString("site");
                mSize = json_object.getInt("size");
                mType = json_object.getString("type");
            } catch (JSONException e) {

            }

            return this;
        }

        public Builder fromDatabase(String content) {

            String[] values = content.split("\\" + DATA_SEPARATOR);

            mId = values[0];
            mISO_639_1 = values[1];
            mKey = values[2];
            mName = values[3];
            mSite = values[4];
            mSize = Utilities.parseInt(values[5]);
            mType = values[6];

            return this;
        }

        public MovieVideo build() {
            return new MovieVideo(this);
        }
    }
}
