package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
     * TextView that is used in case no data can be loaded and the progress bar shown during loading
     */
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
        getSupportActionBar().setTitle("Latest news from The Guardian");


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
            emptyView.setText("No internet connection! :(");
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(NewsActivity.this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        if (emptyView == null) {
            progressBar.setVisibility(View.GONE);
            emptyView.setText("No news articles found!");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
