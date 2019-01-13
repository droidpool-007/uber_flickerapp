package com.flickrapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flickrapp.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;

    public PhotoViewHolder(View view) {
        super(view);
        imageView = view.findViewById(R.id.image_view);
        textView = view.findViewById(R.id.text_view);
    }
}
