package com.example.standard.bookapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vince on 21.03.2017.
 */

public class Utils {

    private static final String LOG_TAG = Utils.class.getName();

    public Utils() {

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(Context context, String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, context.getResources().getString(R.string.malformed_exeption), e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(Context context, URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod(context.getResources().getString(R.string.get_http));
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(context, inputStream);
                //connection = true;
            } else {
                Log.e(LOG_TAG, context.getResources().getString(R.string.error_resp_code) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption), e);
            //connection = false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(Context context, InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(context.getResources().getString(R.string.utf_8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the USGS dataset and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(Context context, String requestUrl) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Create URL object
        URL url = createUrl(context, requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(context, url);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exeption_http), e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(context, jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeatureFromJson(Context context, String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        Drawable thumbnail = context.getResources().getDrawable(R.drawable.thumbnail_icon);

        // Create the first line of the list which contains the concept of the list
        Book bookTwo = new Book(context.getResources().getString(R.string.title_first_line) ,
                context.getResources().getString(R.string.subtitle_first_line),
                context.getResources().getString(R.string.author_first_line),
                context.getResources().getString(R.string.published_first_line), thumbnail);
        books.add(bookTwo);

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of features (or books).
            JSONArray bookArray = baseJsonResponse.getJSONArray(context.getResources().getString(R.string.items_json));

            // For each book in the bookArray, create an {@link Book} object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of informations
                // for that book.
                JSONObject volumeInfo = currentBook.getJSONObject(context.getResources().getString(R.string.volume_info_json));

                // Extract the value for the key called "title"
                String title;
                if (volumeInfo.has(context.getResources().getString(R.string.title_json))) {
                    title = volumeInfo.getString(context.getResources().getString(R.string.title_json));
                } else {
                    title = "";
                }

                // Extract the value for the key called "subtitle"
                String subTitle;
                if (volumeInfo.has(context.getResources().getString(R.string.subtitle_json))) {
                    subTitle = volumeInfo.getString(context.getResources().getString(R.string.subtitle_json));
                } else {
                    subTitle = "";
                }


                // Extract the value for the key called "authors"
                String authors;
                if (volumeInfo.has(context.getResources().getString(R.string.author_volume_json))) {
                    authors = volumeInfo.getString(context.getResources().getString(R.string.author_volume_json));
                } else {
                    authors = "";
                }

                // Extract the value for the key called "url"
                String publishedDate;
                if (volumeInfo.has(context.getResources().getString(R.string.published_json))) {
                    publishedDate = volumeInfo.getString(context.getResources().getString(R.string.published_json));
                } else {
                    publishedDate = "";
                }

                //Extract the JSONObject with the key "imageLinks" which contains the key "smallThumbnail"
                JSONObject imageLinks = volumeInfo.getJSONObject(context.getResources().getString(R.string.imagelinks_json));

                // Extract the value for the key called "smallThumbnail"
                String smallThumbnail;
                if (imageLinks.has(context.getResources().getString(R.string.smallthumbnails_json))) {
                    smallThumbnail = imageLinks.getString(context.getResources().getString(R.string.smallthumbnails_json));
                } else {
                    smallThumbnail = "";
                }

                //Extract the JSONObject with the key "accessInfo" which contains the keya
                // "infoLink" and "webReaderLink"
                JSONObject accessInfo = currentBook.getJSONObject(context.getResources().getString(R.string.accessinfo_json));

                String webReaderLink;
                if (volumeInfo.has(context.getResources().getString(R.string.infolink_json))) {
                    webReaderLink = accessInfo.getString(context.getResources().getString(R.string.webreaderlink_json));
                } else if (accessInfo.has(context.getResources().getString(R.string.webreaderlink_json))) {
                    webReaderLink = volumeInfo.getString(context.getResources().getString(R.string.infolink_json));
                } else {
                    webReaderLink = "";
                }

                // Create a new {@link Book} object with the title, subTitle, authors,
                // publishedDate, smallThumbnail and webReaderLink from the JSON response.
                Book book = new Book(title, subTitle, authors, publishedDate, smallThumbnail, webReaderLink);
                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, context.getResources().getString(R.string.io_exepion_three), e);
        }

        // Return the list of books
        return books;
    }

}
