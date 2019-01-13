package com.flickrapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flickrapp.imageloader.ImageLoader;
import com.flickrapp.pojo.Photo;
import com.flickrapp.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private Context context;
    private ImageLoader imageLoader;
    private List<Photo> photos = new ArrayList<>();
    private OnBottomReachedListener mOnBottomReachedListener;
    private boolean isLoading = false;

    public PhotoAdapter(Context context) {
        this.context = context;
        imageLoader = new ImageLoader(context);
    }

    public void submitList(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        String url = context.getResources().getString(R.string.flickr_image_url, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
        imageLoader.loadImage(url, holder.imageView);

        holder.textView.setText(photo.getTitle());

        if (position == photos.size() - 1 && !isLoading && mOnBottomReachedListener !=null){
            isLoading = true;
            mOnBottomReachedListener.onBottomReached();
        }
    }

    @Override
    public int getItemCount() {
        return (photos == null) ? 0 : photos.size();
    }

    public void setOnBottomReachedListener(OnBottomReachedListener listener) {
        mOnBottomReachedListener = listener;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }

}