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

    ViewHolderItem viewHolder;


    public BookAdapter(Context context, ArrayList<Book> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolderItem {
        TextView title, subTitle, authors, publishedDate;
        ImageView bookImage;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

            viewHolder = new ViewHolderItem();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title_text);
            viewHolder.subTitle = (TextView) convertView.findViewById(R.id.subtitle_text);
            viewHolder.authors = (TextView) convertView.findViewById(R.id.author_text);
            viewHolder.publishedDate = (TextView) convertView.findViewById(R.id.published_text);
            viewHolder.bookImage = (ImageView)  convertView.findViewById(R.id.book_image);

            // store the holder with the view
            convertView.setTag(viewHolder);
        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // Get the {@link Word} object located at this position in the list
        Book currentBook = getItem(position);

        //Fit in the values in the items
        if (currentBook.getmSmallThumbnail() == null || currentBook.getmSmallThumbnail().equals("")) {
            viewHolder.bookImage.setImageDrawable(currentBook.getThumbnail());
        } else if (currentBook.getmSmallThumbnail() != null && !currentBook.getmSmallThumbnail().equals("")){
            Glide.with(getContext()).load(currentBook.getmSmallThumbnail()).into(viewHolder.bookImage);
        }

        viewHolder.title.setText(currentBook.getmTitle());
        viewHolder.subTitle.setText(currentBook.getmSubTitle());
        viewHolder.authors.setText(currentBook.getmAutor());
        viewHolder.publishedDate.setText(currentBook.getmPublishedDate());

        return convertView;
    }
}
