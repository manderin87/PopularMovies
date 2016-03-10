package com.hydeudacityproject.popularmovies.Service.Framework;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.lonewolfgames.framework.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jhyde on 11/8/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class MovieDetail extends Movie implements Parcelable {

    private static final String DATA_SEPARATOR = ";";

    public enum MovieDatabaseKeys {
        _Id                     (0,     "_Id"),
        Adult                   (1,     "adult"),
        BackdropPath            (2,     "backdrop_path"),
        GenreIds                (3,     "genre_ids"),
        Id                      (4,     "id"),
        OriginalLanguage        (5,     "original_Language"),
        OriginalTitle           (6,     "original_title"),
        Overview                (7,     "overview"),
        ReleaseDate             (8,     "release_date"),
        PosterPath              (9,     "poster_path"),
        Popularity              (10,    "popularity"),
        Title                   (11,    "title"),
        Video                   (12,    "video"),
        VoteAverage             (13,    "vote_average"),
        VoteCount               (14,    "vote_count"),
        Budget                  (15,    "budget"),
        Genres                  (16,    "genres"),
        IMDBId                  (17,    "imdb_id"),
        ProductionCompanies     (18,    "production_companies"),
        ProductionCountries     (19,    "production_countries"),
        Revenue                 (20,    "revenue"),
        Runtime                 (21,    "runtime"),
        SpokenLanguages         (22,    "spoken_languages"),
        Status                  (23,    "status"),
        Tagline                 (24,    "tagline"),
        Videos                  (25,    "videos"),
        VideosLoaded            (26,    "videos_loaded"),
        Reviews                 (27,    "reviews"),
        ReviewsLoaded           (28,    "reviews_loaded");

        private int mKeyPosition = -1;
        private String mKeyName = "";

        MovieDatabaseKeys(int keyPosition, String keyName) {
            mKeyPosition = keyPosition;
            mKeyName = keyName;
        }

        public int keyPosition() { return mKeyPosition; }
        public String keyName() { return mKeyName; }
    }

    private int mBudget = 0;
    private HashMap<Integer, String> mGenres;
    private String mIMDBId = "";
    private HashMap<Integer, String> mProductionCompanies;
    private HashMap<String, String> mProductionCountries;
    private int mRevenue = 0;
    private int mRuntime = 0;
    private HashMap<String, String> mSpokenLanguages;
    private String mStatus = "";
    private String mTagLine = "";
    private ArrayList<MovieVideo> mVideos;
    private boolean mVideosLoaded = false;
    private ArrayList<MovieReview> mReviews;
    private boolean mReviewsLoaded = false;

    public int budget() { return mBudget; }
    public String genres() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Map.Entry<Integer, String> entry : mGenres.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            if (count < mGenres.size() - 1) {
                sb.append(value + " | ");
            } else {
                sb.append(value);
            }

            count++;
        }

        return sb.toString();
    }

    public String genresInformation() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for(Map.Entry<Integer, String> entry : mGenres.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();

            if (count < mGenres.size() - 1) {
                sb.append(key + "=" + value + DATA_SEPARATOR);
            } else {
                sb.append(key + "=" + value);
            }

            count++;
        }

        return sb.toString();
    }


    public String idmbId() { return mIMDBId; }
    public String productionCompaniesInformation() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for(Map.Entry<Integer, String> entry : mProductionCompanies.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();

            if (count < mProductionCompanies.size() - 1) {
                sb.append(key + "=" + value + DATA_SEPARATOR);
            } else {
                sb.append(key + "=" + value);
            }

            count++;
        }

        return sb.toString();
    }
    public String productionCountriesInformation() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for(Map.Entry<String, String> entry : mProductionCountries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (count < mProductionCountries.size() - 1) {
                sb.append(key + "=" + value + DATA_SEPARATOR);
            } else {
                sb.append(key + "=" + value);
            }

            count++;
        }

        return sb.toString();
    }
    public int revenue() { return mRevenue; }
    public int runtime() { return mRuntime; }
    public String spokenLanguagesInformation() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for(Map.Entry<String, String> entry : mSpokenLanguages.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (count < mSpokenLanguages.size() - 1) {
                sb.append(key + "=" + value + DATA_SEPARATOR);
            } else {
                sb.append(key + "=" + value);
            }

            count++;
        }

        return sb.toString();
    }
    public String status() { return mStatus; }
    public String tagline() { return mTagLine; }
    public String videosInformation() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for(MovieVideo video : mVideos) {

            if (count < mVideos.size() - 1) {
                sb.append(video.asString() + DATA_SEPARATOR);
            } else {
                sb.append(video.asString());
            }

            count++;
        }

        return sb.toString();
    }
    public String reviewsInformation() {
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for(MovieReview review : mReviews) {

            if (count < mReviews.size() - 1) {
                sb.append(review.asString() + DATA_SEPARATOR);
            } else {
                sb.append(review.asString());
            }

            count++;
        }

        return sb.toString();
    }
    public ArrayList<MovieVideo> videosList() { return mVideos; }
    public boolean isVideosLoaded() { return mVideosLoaded; }
    public boolean hasVideos() { return mVideos.size() > 0 ? true : false; }
    public ArrayList<MovieReview> reviewsList() { return mReviews; }
    public boolean isReviewsLoaded() { return mReviewsLoaded; }
    public boolean hasReviews() { return mReviews.size() > 0 ? true : false; }

    public void setVideosList(ArrayList<MovieVideo> videos) {
        mVideosLoaded = true;
        mVideos = videos;
    }
    public void setReviewsList(ArrayList<MovieReview> reviews) {
        mReviewsLoaded = true;
        mReviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeValue(mGenres);
        dest.writeString(mIMDBId);
        dest.writeValue(mProductionCompanies);
        dest.writeValue(mProductionCountries);
        dest.writeInt(mRevenue);
        dest.writeInt(mRuntime);
        dest.writeValue(mSpokenLanguages);
        dest.writeString(mStatus);
        dest.writeString(mTagLine);
        dest.writeValue(mVideos);
        dest.writeValue(mVideosLoaded);
        dest.writeValue(mReviews);
        dest.writeValue(mReviewsLoaded);
    }

    private MovieDetail(Parcel in) {
        super(in);

        mGenres = (HashMap<Integer, String>) in.readValue(HashMap.class.getClassLoader());
        mIMDBId = in.readString();
        mProductionCompanies = (HashMap<Integer, String>) in.readValue(HashMap.class.getClassLoader());
        mProductionCountries = (HashMap<String, String>) in.readValue(HashMap.class.getClassLoader());
        mRevenue = in.readInt();
        mRuntime = in.readInt();
        mSpokenLanguages = (HashMap<String, String>) in.readValue(HashMap.class.getClassLoader());
        mStatus = in.readString();
        mTagLine = in.readString();
        mVideos = (ArrayList<MovieVideo>) in.readValue(MovieVideo.class.getClassLoader());
        mVideosLoaded = (Boolean) in.readValue(Boolean.class.getClassLoader());
        mReviews = (ArrayList<MovieReview>) in.readValue(MovieReview.class.getClassLoader());
        mReviewsLoaded = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR
            = new Parcelable.Creator<MovieDetail>() {

        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };

    private MovieDetail(Builder build) {
        super(build);

        mBudget = build.mBudget;
        mGenres = build.mGenres;
        mIMDBId = build.mIMDBId;
        mProductionCompanies = build.mProductionCompanies;
        mProductionCountries = build.mProductionCountries;
        mRevenue = build.mRevenue;
        mRuntime = build.mRuntime;
        mSpokenLanguages = build.mSpokenLanguages;
        mStatus = build.mStatus;
        mTagLine = build.mTagLine;
        mVideos = build.mVideos;
        mVideosLoaded = build.mVideosLoaded;
        mReviews = build.mReviews;
        mReviewsLoaded = build.mReviewsLoaded;
    }

    public static class Builder extends Movie.Builder {
        private int mBudget = 0;
        private HashMap<Integer, String> mGenres = new HashMap<>();
        private String mIMDBId = "";
        private HashMap<Integer, String> mProductionCompanies = new HashMap<>();
        private HashMap<String, String> mProductionCountries = new HashMap<>();
        private int mRevenue = 0;
        private int mRuntime = 0;
        private HashMap<String, String> mSpokenLanguages = new HashMap<>();
        private String mStatus = "";
        private String mTagLine = "";
        private ArrayList<MovieVideo> mVideos = new ArrayList<>();
        private boolean mVideosLoaded = false;
        private ArrayList<MovieReview> mReviews = new ArrayList<>();
        private boolean mReviewsLoaded = false;

        public Builder() {}

        public Builder fromJSON(String content) {
            super.fromJSON(content);

            try {

                JSONObject json_object = new JSONObject(content);

                mBudget = json_object.getInt("budget");

                JSONArray genres_array = json_object.getJSONArray("genres");
                for(int index = 0; index < genres_array.length(); index++) {
                    int id = genres_array.getJSONObject(index).getInt("id");
                    String name = genres_array.getJSONObject(index).getString("name");

                    mGenres.put(id, name);
                }

                mIMDBId = json_object.getString("imdb_id");

                JSONArray companies_array = json_object.getJSONArray("production_companies");
                for(int index = 0; index < companies_array.length(); index++) {
                    int id = companies_array.getJSONObject(index).getInt("id");
                    String name = companies_array.getJSONObject(index).getString("name");

                    mProductionCompanies.put(id, name);
                }

                JSONArray countries_array = json_object.getJSONArray("production_countries");
                for(int index = 0; index < countries_array.length(); index++) {
                    String id = countries_array.getJSONObject(index).getString("iso_3166_1");
                    String name = countries_array.getJSONObject(index).getString("name");

                    mProductionCountries.put(id, name);
                }

                mRevenue = json_object.getInt("revenue");
                mRuntime = json_object.getInt("runtime");

                JSONArray languages_array = json_object.getJSONArray("spoken_languages");
                for(int index = 0; index < languages_array.length(); index++) {
                    String id = languages_array.getJSONObject(index).getString("iso_639_1");
                    String name = languages_array.getJSONObject(index).getString("name");

                    mSpokenLanguages.put(id, name);
                }

                mStatus = json_object.getString("status");
                mTagLine = json_object.getString("tagline");


            } catch (JSONException e) {

            }

            return this;
        }

        public Builder fromCursor(Cursor cursor) {
            super.fromCursor(cursor);

            mBudget = cursor.getInt(MovieDatabaseKeys.Budget.keyPosition());

            parseArrayDataIntStr(cursor.getString(MovieDatabaseKeys.Genres.keyPosition()),
                    mGenres);

            mIMDBId = cursor.getString(MovieDatabaseKeys.IMDBId.keyPosition());

            parseArrayDataIntStr(cursor.getString(MovieDatabaseKeys.ProductionCompanies.keyPosition()),
                    mProductionCompanies);

            parseArrayDataStrStr(cursor.getString(MovieDatabaseKeys.ProductionCountries.keyPosition()),
                    mProductionCountries);

            mRevenue = cursor.getInt(MovieDatabaseKeys.Revenue.keyPosition());
            mRuntime = cursor.getInt(MovieDatabaseKeys.Runtime.keyPosition());

            parseArrayDataStrStr(cursor.getString(MovieDatabaseKeys.SpokenLanguages.keyPosition()),
                    mSpokenLanguages);

            mStatus = cursor.getString(MovieDatabaseKeys.Status.keyPosition());
            mTagLine = cursor.getString(MovieDatabaseKeys.Tagline.keyPosition());

            mVideos = parseVideos(cursor.getString(MovieDatabaseKeys.Videos.keyPosition()));
            mVideosLoaded = cursor.getInt(MovieDatabaseKeys.VideosLoaded.keyPosition()) == 1 ? true : false;

            mReviews = parseReviews(cursor.getString(MovieDatabaseKeys.Reviews.keyPosition()));
            mReviewsLoaded = cursor.getInt(MovieDatabaseKeys.ReviewsLoaded.keyPosition()) == 1 ? true : false;

            return this;
        }

        public MovieDetail build() {
            return new MovieDetail(this);
        }

        private void parseArrayDataIntStr(String data, HashMap<Integer, String> container) {
            String[] split_companies = data.split("\\" + DATA_SEPARATOR);

            for(int index = 0; index < split_companies.length; index++) {
                String str = split_companies[index];

                String[] values = str.split("=");

                int id = Utilities.parseInt(values[0]);
                String name = values[1];

                container.put(id, name);
            }
        }

        private void parseArrayDataStrStr(String data, HashMap<String, String> container) {
            String[] split_companies = data.split("\\" + DATA_SEPARATOR);

            for(int index = 0; index < split_companies.length; index++) {
                String str = split_companies[index];

                String[] values = str.split("=");

                String id = values[0];
                String name = values[1];

                container.put(id, name);
            }
        }

        private ArrayList<MovieVideo> parseVideos(String data) {
            ArrayList<MovieVideo> videos = new ArrayList<>();

            if(data.isEmpty()) {
                return videos;
            }

            String[] split_videos = data.split("\\" + DATA_SEPARATOR);

            for(int index = 0; index < split_videos.length; index++) {
                String video = split_videos[index];


                videos.add(new MovieVideo.Builder()
                        .fromDatabase(video)
                        .build());
            }

            return videos;
        }

        private ArrayList<MovieReview> parseReviews(String data) {
            ArrayList<MovieReview> reviews = new ArrayList<>();

            if(data.isEmpty()) {
                return reviews;
            }

            String[] split_reviews = data.split("\\" + DATA_SEPARATOR);



            for(int index = 0; index < split_reviews.length; index++) {
                String review = split_reviews[index];


                reviews.add(new MovieReview.Builder()
                        .fromDatabase(review)
                        .build());
            }

            return reviews;
        }
    }
}
