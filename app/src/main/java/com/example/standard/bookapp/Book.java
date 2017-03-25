package com.example.standard.bookapp;

import android.graphics.drawable.Drawable;

/**
 * Created by vince on 21.03.2017.
 */

public class Book {

    private String mTitle, mSubTitle, mAutor, mPublishedDate, mSmallThumbnail, mWebReaderLink;

    Drawable thumbnail;

    public Book(){

    }

    public Book(String mTitle, String mSubTitle, String mAutor,
                String mPublishedDate, String mSmallThumbnail, String mWebReaderLink) {
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
        this.mAutor = mAutor;
        this.mPublishedDate = mPublishedDate;
        this.mSmallThumbnail = mSmallThumbnail;
        this.mWebReaderLink = mWebReaderLink;
    }

    public Book(String mTitle, String mSubTitle, String mAutor, String mPublishedDate, Drawable thumbnail) {
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
        this.mAutor = mAutor;
        this.mPublishedDate = mPublishedDate;
        this.thumbnail = thumbnail;
    }

    public Drawable getThumbnail(){
        return thumbnail;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSubTitle() {
        return mSubTitle;
    }

    public String getmAutor() {
        return mAutor;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public String getmSmallThumbnail() {
        return mSmallThumbnail;
    }

    public String getmWebReaderLink() {
        return mWebReaderLink;
    }
}
