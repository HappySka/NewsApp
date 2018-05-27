package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (!TextUtils.isEmpty(mUrl)) {
            return QueryUtils.fetchNewsData(mUrl);
        } else
            return null;

    }
}
