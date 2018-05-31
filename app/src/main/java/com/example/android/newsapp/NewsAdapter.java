package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A NewsAdapters knows how to create a list item layout for each news article
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
class NewsAdapter extends ArrayAdapter<News> {

    private Context mContext;

    /**
     * Constructs a new NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news articles, which is the data source of the adapter
     */
    NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
        mContext = context;
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        ViewHolder holder;
        //If the view is reused, use the held views in the already existing ViewHolder
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
            //Create a new ViewHolder and set it as tag so it can be reused
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        }

        News currentNews = getItem(position);

        //sets the News section
        holder.categoryView.setText(currentNews.getNewsSection());


        //sets the News title
        holder.titleView.setText(currentNews.getNewsTitle());


        //set the Date and Authors
        StringBuilder dateAndAuthors = new StringBuilder();

        //get date and format it
        String date = formatDate(currentNews.getNewsDate());
        dateAndAuthors.append(date);

        //get list of author(s) and format it
        List<String> authors = currentNews.getNewsAuthors();
        if (authors.size() > 0) {
            dateAndAuthors.append(mContext.getString(R.string.date_authors_seperator));
            for (int i = 0; i < authors.size(); i++) {
                if (i >= 1) {
                    dateAndAuthors.append(mContext.getString(R.string.author_seperator));
                }
                dateAndAuthors.append(authors.get(i));
            }
        }
        //automatic call of the toString method
        holder.dateView.setText(dateAndAuthors);

        return listItemView;
    }

    private String formatDate(DateTime date) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY HH:mm");
        return fmt.print(date);
    }

    /**
     * holds the views to the TextViews in the list_item so findViewById does not need to be called
     * everytime a list item is updated
     */
    static class ViewHolder {
        @BindView(R.id.item_category)
        TextView categoryView;
        @BindView(R.id.item_title)
        TextView titleView;
        @BindView(R.id.item_date_author)
        TextView dateView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
