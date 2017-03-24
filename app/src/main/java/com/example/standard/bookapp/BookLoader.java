package com.example.standard.bookapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by vince on 21.03.2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    public BookLoader(Context context, String mUrl) {
        super(context);

        this.mUrl = mUrl;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Book> books = Utils.fetchBookData(getContext(), mUrl);

        return books;
    }
}
