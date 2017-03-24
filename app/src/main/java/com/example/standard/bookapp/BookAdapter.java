package com.example.standard.bookapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by vince on 21.03.2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {


    public BookAdapter(Context context, ArrayList<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Book currentBook = getItem(position);

        //Fit in the values in the items
        ImageView bookImage = (ImageView) convertView.findViewById(R.id.book_image);
        if (position == 0 || currentBook.getmSmallThumbnail().equals("")) {
            bookImage.setImageDrawable(currentBook.getThumbnail());
        } else {
            Glide.with(getContext()).load(currentBook.getmSmallThumbnail()).into(bookImage);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title_text);
        title.setText(currentBook.getmTitle());

        TextView subTitle = (TextView) convertView.findViewById(R.id.subtitle_text);
        subTitle.setText(currentBook.getmSubTitle());

        TextView authors = (TextView) convertView.findViewById(R.id.author_text);
        authors.setText(currentBook.getmAutor());

        TextView publishedDate = (TextView) convertView.findViewById(R.id.published_text);
        publishedDate.setText(currentBook.getmPublishedDate());

        return convertView;
    }
}
