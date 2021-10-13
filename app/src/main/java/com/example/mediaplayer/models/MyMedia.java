package com.example.mediaplayer.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

public class MyMedia {

    long id;
    Uri uri;
    String names, title, album, artist, dateAdded;
    String duration, size;

    public MyMedia(long id, Uri uri, Bitmap thumbnail, String names, String title, String album, String artist, String dateAdded, String duration, String size) {
        this.id = id;
        this.uri = uri;
        this.names = names;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.dateAdded = dateAdded;
        this.duration = duration;
        this.size = size;
    }

    public String getNames() {
        return names;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDuration() {
        return duration;
    }

    public String getSize() {
        return size;
    }

    public long getId() {
        return id;
    }

    public Uri getUri() {
        return uri;
    }

}
