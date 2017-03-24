package com.example.standard.bookapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {



    private static final String BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    //Resources.getSystem().getString(android.R.string.somecommonstuff)
    private String mUrl, mQuery;
    private BookAdapter mAdapter;

    ListView bookListView;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        bookListView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = mAdapter.getItem(position);

                if (!currentBook.getmWebReaderLink().equals("")){
                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri bookUri = Uri.parse(currentBook.getmWebReaderLink());

                    // Create a new intent to view the earthquake URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_web_available), Toast.LENGTH_LONG).show();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mQuery = extras.getString(getString(R.string.intent_key));
        }

        mUrl = BOOKS_REQUEST_URL + mQuery;
        //Log.i("Test", "Url = " + mUrl);
        LoaderManager loader = getLoaderManager();

        loader.initLoader(0, null, this);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        return new BookLoader(getApplicationContext(), mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);

        // Clear the adapter of previous book data
        mAdapter.clear();

        if (books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        } else {
            bookListView = (ListView) findViewById(R.id.list);
            bookListView.setEmptyView(findViewById(R.id.empty_view));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
