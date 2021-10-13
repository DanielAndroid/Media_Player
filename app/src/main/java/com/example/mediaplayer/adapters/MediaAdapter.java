package com.example.mediaplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mediaplayer.R;
import com.example.mediaplayer.models.MyMedia;

import java.util.List;


public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    List<MyMedia> mediaList;
    Context context;
    OnMediaItemClickListener onMediaItemClickListener;
    private Bitmap thumbnail;


    public MediaAdapter(Context context, List<MyMedia> mediaList, OnMediaItemClickListener onMediaItemClickListener) {
        this.context = context;
        this.mediaList = mediaList;
        this.onMediaItemClickListener = onMediaItemClickListener;
    }


    public static class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       private final TextView titleTextView;
       private final TextView durationTextVew;
       private OnMediaItemClickListener onMediaItemClickListener;

        public MediaViewHolder(@NonNull View view, OnMediaItemClickListener onMediaItemClickListener) {
            super(view);

            titleTextView = view.findViewById(R.id.title_text_view);
            durationTextVew = view.findViewById(R.id.duration_text_view);
            this.onMediaItemClickListener = onMediaItemClickListener;
            view.setOnClickListener(this);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDurationTextVew() {
            return durationTextVew;
        }

        @Override
        public void onClick(View view) {
          onMediaItemClickListener.onMediaItemClick(getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_item, parent, false);
        return new MediaViewHolder(view, onMediaItemClickListener);


    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        thumbnail = null;
        MyMedia media = mediaList.get(position);
        holder.getTitleTextView().setText(media.getTitle());
        holder.getDurationTextVew().setText(media.getDuration());
    }

    public  interface OnMediaItemClickListener {
        void onMediaItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
}

