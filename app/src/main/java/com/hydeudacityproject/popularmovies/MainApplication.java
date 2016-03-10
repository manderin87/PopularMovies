package com.hydeudacityproject.popularmovies;

import com.lonewolfgames.framework.AbstractMainApplication;
import com.lonewolfgames.framework.Cache.Files.FileCache;
import com.lonewolfgames.framework.Cache.Images.ImageCache;

/**
 * Created by jhyde on 11/7/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */
public class MainApplication extends AbstractMainApplication {

    private static MainApplication mInstance;

    private AppSettings     mAppSettings;
    private AppData         mAppData;

    private FileCache       mCache;
    private ImageCache      mImageCache;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mCache = new FileCache(this);

        mAppData = new AppData(this);
        mAppSettings = new AppSettings(this, mAppData);

        mImageCache = new ImageCache(this);
    }



    @Override
    public AbstractMainApplication instance() {
        return (MainApplication) mInstance;
    }
}
