package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * URL for news JSON from the Guardian that shows the 10 newest article together with information
     * about the author(s)
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?&show-tags=contributor&api-key=test";
    /**
     * ListView, TextView that is used in case no data can be loaded and the progress bar shown during loading
     */
    @BindView(R.id.list)
    ListView newsListView;
    @BindView(R.id.empty_text)
    TextView emptyView;
    @BindView(R.id.loading_spinner)
    ProgressBar progressBar;
    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        ButterKnife.bind(this);

        //Set Action Bar Text
        getSupportActionBar().setTitle(R.string.action_bar_title);

        //Set empty view for the list view
        newsListView.setEmptyView(emptyView);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open the actual news article
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentNews.getNewsUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        //Check for internet connection
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Start the AsyncTask to fetch the earthquake data
            getLoaderManager().initLoader(0, null, this);
        } else {
            progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            emptyView = findViewById(R.id.empty_text);
            emptyView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(NewsActivity.this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        mAdapter.clear();

        // If there is a valid list of News, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        progressBar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            if (QueryUtils.lastHttpResponse == 200) {
                emptyView.setText(R.string.no_news_found);
            } else {
                emptyView.setText(R.string.no_news_http_problem);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
