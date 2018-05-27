package com.example.android.newsapp;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * A News object contains information regarding a single news article
 */
class News {

    //Title, list of authors, name of the news section, date of publishing and URL to the article
    private String mTitle;
    private ArrayList<String> mAuthors;
    private String mSection;
    private DateTime mDate;
    private String mArticleUrl;

    News(String title, ArrayList<String> authors, String section, DateTime date, String articleUrl) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mSection = section;
        this.mDate = date;
        this.mArticleUrl = articleUrl;
    }

    public String getNewsTitle() {
        return mTitle;
    }

    public ArrayList<String> getNewsAuthors() {
        return mAuthors;
    }

    public String getNewsSection() {
        return mSection;
    }

    public DateTime getNewsDate() {
        return mDate;
    }

    public String getNewsArticleUrl() {
        return mArticleUrl;
    }

}
