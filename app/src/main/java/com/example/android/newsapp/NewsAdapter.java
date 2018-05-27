package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
