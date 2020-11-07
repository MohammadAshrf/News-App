package com.example.newsapp;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String mUrl;

    public NewsLoader(Context context, String url) {
        super (context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.getNewsData (mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad ();
    }
}
