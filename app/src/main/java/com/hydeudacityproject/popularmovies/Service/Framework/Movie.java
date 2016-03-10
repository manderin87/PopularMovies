package com.hydeudacityproject.popularmovies.Service.Framework;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hydeudacityproject.popularmovies.Service.MovieService;
import com.lonewolfgames.framework.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhyde on 10/28/2015.
 */
public class Movie implements Parcelable {

    public enum MovieDatabaseKeys {
        _Id                    (0,     "_Id"),
        Adult                  (1,     "adult"),
        BackdropPath           (2,     "backdrop_path"),
        GenreIds               (3,     "genre_ids"),
        Id                     (4,     "id"),
        OriginalLanguage       (5,     "original_Language"),
        OriginalTitle          (6,     "original_title"),
        Overview               (7,     "overview"),
        ReleaseDate            (8,     "release_date"),
        PosterPath             (9,     "poster_path"),
        Popularity             (10,    "popularity"),
        Title                  (11,    "title"),
        Video                  (12,    "video"),
        VoteAverage            (13,    "vote_average"),
        VoteCount              (14,    "vote_count");

        private int mKeyPosition = -1;
        private String mKeyName = "";

        MovieDatabaseKeys(int keyPosition, String keyName) {
            mKeyPosition = keyPosition;
            mKeyName = keyName;
        }

        public int keyPosition() { return mKeyPosition; }
        public String keyName() { return mKeyName; }
    }

    protected boolean mAdult = false;
    protected String mBackdropPath = "";
    protected ArrayList<Integer> mGenreIds;
    protected int mId = 0;
    protected String mOriginalLanguage = "";
    protected String mOriginalTitle = "";
    protected String mOverview = "";
    protected Date mReleaseDate = new Date();
    protected String mPosterPath = "";
    protected double mPopularity = 0.0f;
    protected String mTitle = "";
    protected boolean mVideo = false;
    protected double mVoteAverage = 0.0f;
    protected int mVoteCount = 0;

    public boolean adult() { return mAdult; }
    public String backdropImage() { return "http://image.tmdb.org/t/p/w500/" + mBackdropPath; }
    public String backdropPath() { return mBackdropPath; }
    public String genreIds() {
        return mGenreIds.toString();
    }
    public int id() { return mId; }
    public String originalLanguage() { return mOriginalLanguage; }
    public String originalTitle() { return mOriginalTitle; }
    public String overview() { return mOverview; }
    public String releaseDateAsString() { return MovieService.RELEASE_DATE_FORMAT.format(mReleaseDate); }
    public Date releaseDateAsDate() { return mReleaseDate; }
    public String posterImage() { return "http://image.tmdb.org/t/p/w500/" + mPosterPath; }
    public String posterPath() { return mPosterPath; }
    public double popularity() { return mPopularity; }
    public String title() { return mTitle; }
    public boolean video() { return mVideo; }
    public double voteAverage() { return mVoteAverage; }
    public int voteCount() { return mVoteCount; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mAdult);
        dest.writeString(mBackdropPath);
        dest.writeValue(mGenreIds);
        dest.writeInt(mId);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeValue(mReleaseDate);
        dest.writeString(mPosterPath);
        dest.writeDouble(mPopularity);
        dest.writeString(mTitle);
        dest.writeValue(mVideo);
        dest.writeDouble(mVoteAverage);
        dest.writeInt(mVoteCount);
    }

    protected Movie(Parcel in) {
        mAdult = (Boolean) in.readValue(Boolean.class.getClassLoader());
        mBackdropPath = in.readString();
        mGenreIds = (ArrayList<Integer>) in.readValue(ArrayList.class.getClassLoader());
        mId = in.readInt();
        mOriginalLanguage = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = (Date) in.readValue(Date.class.getClassLoader());
        mPosterPath = in.readString();
        mPopularity = in.readDouble();
        mTitle = in.readString();
        mVideo = (Boolean) in.readValue(Boolean.class.getClassLoader());
        mVoteAverage = in.readDouble();
        mVoteCount = in.readInt();
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    protected Movie(Builder build) {
        mAdult = build.mAdult;
        mBackdropPath = build.mBackdropPath;
        mGenreIds = build.mGenreIds;
        mId = build.mId;
        mOriginalLanguage = build.mOriginalLanguage;
        mOriginalTitle = build.mOriginalTitle;
        mOverview = build.mOverview;
        mReleaseDate = build.mReleaseDate;
        mPosterPath = build.mPosterPath;
        mPopularity = build.mPopularity;
        mTitle = build.mTitle;
        mVideo = build.mVideo;
        mVoteAverage = build.mVoteAverage;
        mVoteCount = build.mVoteCount;
    }

    public static class Builder {
        protected boolean mAdult = false;
        protected String mBackdropPath = "";
        protected ArrayList<Integer> mGenreIds = new ArrayList<>();
        protected int mId = 0;
        protected String mOriginalLanguage = "";
        protected String mOriginalTitle = "";
        protected String mOverview = "";
        protected Date mReleaseDate = new Date();
        protected String mPosterPath = "";
        protected double mPopularity = 0.0f;
        protected String mTitle = "";
        protected boolean mVideo = false;
        protected double mVoteAverage = 0.0f;
        protected int mVoteCount = 0;

        public Builder() {}

        public Builder fromJSON(String content) {
            try {
                JSONObject json_object = new JSONObject(content);

                mAdult = json_object.getBoolean("adult");
                mBackdropPath = json_object.getString("backdrop_path");

                if(json_object.has("genre_ids")) {
                    JSONArray json_array = json_object.getJSONArray("genre_ids");
                    for(int index = 0; index < json_array.length(); index++) {
                        int id = Utilities.parseInt(json_array.getString(index));

                        mGenreIds.add(id);
                    }
                }

                mId = json_object.getInt("id");
                mOriginalLanguage = json_object.getString("original_language");
                mOriginalTitle = json_object.getString("original_title");
                mOverview = json_object.getString("overview");
                mReleaseDate = MovieService.RELEASE_DATE_FORMAT.parse(json_object.getString("release_date"));
                mPosterPath = json_object.getString("poster_path");
                mPopularity = json_object.getDouble("popularity");
                mTitle = json_object.getString("title");
                mVideo = json_object.getBoolean("video");
                mVoteAverage = json_object.getDouble("vote_average");
                mVoteCount = json_object.getInt("vote_count");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return this;
        }



        public Builder fromCursor(Cursor cursor) {


                mAdult = cursor.getInt(MovieDatabaseKeys.Adult.keyPosition()) == 1 ? true : false;
                mBackdropPath = cursor.getString(MovieDatabaseKeys.BackdropPath.keyPosition());

                String genre_ids = cursor.getString(MovieDatabaseKeys.GenreIds.keyPosition());

//                if(json_object.has("genre_ids")) {
//                    JSONArray json_array = json_object.getJSONArray("genre_ids");
//                    for(int index = 0; index < json_array.length(); index++) {
//                        int id = Utilities.parseInt(json_array.getString(index));
//
//                        mGenreIds.add(id);
//                    }
//                }

                mId = cursor.getInt(MovieDatabaseKeys.Id.keyPosition());
                mOriginalLanguage = cursor.getString(MovieDatabaseKeys.OriginalLanguage.keyPosition());
                mOriginalTitle = cursor.getString(MovieDatabaseKeys.OriginalTitle.keyPosition());
                mOverview = cursor.getString(MovieDatabaseKeys.Overview.keyPosition());
                mReleaseDate = Utilities.parseDate(
                        MovieService.RELEASE_DATE_FORMAT,
                        cursor.getString(MovieDatabaseKeys.ReleaseDate.keyPosition()));
                mPosterPath = cursor.getString(MovieDatabaseKeys.PosterPath.keyPosition());
                mPopularity = cursor.getDouble(MovieDatabaseKeys.Popularity.keyPosition());
                mTitle = cursor.getString(MovieDatabaseKeys.Title.keyPosition());
                mVideo = cursor.getInt(MovieDatabaseKeys.Video.keyPosition()) == 1 ? true : false;
                mVoteAverage = cursor.getDouble(MovieDatabaseKeys.VoteAverage.keyPosition());
                mVoteCount = cursor.getInt(MovieDatabaseKeys.VoteCount.keyPosition());

            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}
