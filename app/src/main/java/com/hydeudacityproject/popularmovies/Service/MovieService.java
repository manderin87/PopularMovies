package com.hydeudacityproject.popularmovies.Service;

import com.lonewolfgames.framework.AbstractAppService;

import java.text.SimpleDateFormat;

/**
 * Created by jhyde on 11/9/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class MovieService extends AbstractAppService {

    public static final SimpleDateFormat RELEASE_DATE_FORMAT = new SimpleDateFormat("yyyy-dd-MM");
    public static final SimpleDateFormat RELEASE_DATE_YEAR_FORMAT = new SimpleDateFormat("yyyy");

    public static final String MOVIE_API_KEY = "requires_key";
    public static final String YOUTUBE_API_KEY = "requires_key";


    public static final String MOVIE_API_BASE_URL = "http://api.themoviedb.org/3";
    public static final String MOVIE_API_DISCOVER_MOVIE = "/discover/movie";
    public static final String MOVIE_API_MOVIE = "/movie";
    public static final String MOVIE_API_SORT_BY_POPULARITY = "sort_by=popularity.desc";
    public static final String MOVIE_API_SORT_BY_VOTE_COUNT = "sort_by=vote_count.desc";
    public static final String MOVIE_API_SORT_BY_VOTE_AVERAGE = "sort_by=vote_average.desc";
    public static final String MOVIE_API_TOP_RATED = "/top_rated";
    public static final String MOVIE_API_POPULAR = "/popular";
    public static final String MOVIE_API_UPCOMING = "/upcoming";
    public static final String MOVIE_API_NOW_PLAYING = "/now_playing";
    public static final String MOVIE_API_PAGE = "page=";

    public static final String MOVIE_API_MOVIE_DETAIL = "/movie/";
    public static final String MOVIE_API_MOVIE_VIDEOS = "/videos";
    public static final String MOVIE_API_MOVIE_REVIEWS = "/reviews";
    public static final String MOVIE_API_URL_START = "?";
    public static final String MOVIE_ADI_URL_SEPARATOR = "&";


    public enum ResultType {
        Popular,
        HighestRated,
        Upcoming,
        NowPlaying,
        Favorites
    }
}
