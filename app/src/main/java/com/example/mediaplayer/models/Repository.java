package com.example.mediaplayer.models;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Size;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Repository {

    Context context;
    ArrayList<MyMedia> mediaList = new ArrayList<>();


    Uri collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);


    String[] projection = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };

    String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

    public ArrayList<MyMedia> getMedia(Context context) {
        Bitmap thumbnail = null;
        this.context = context;
        try (
                Cursor cursor = context.getContentResolver().query(
                        collection,
                        projection,
                        null,
                        null,
                        sortOrder
                )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int titleColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int albumColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int artistColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int dateAddedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                String title = cursor.getString(titleColumn);
                String album = cursor.getString(albumColumn);
                String artist = cursor.getString(artistColumn);
                String dateAdded = cursor.getString(dateAddedColumn);
                long duration = cursor.getLong(durationColumn);
                long size = cursor.getLong(sizeColumn);

                long seconds = duration / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                String songDuration;

                long kb = size / 1024;
                long mb = kb / 1024;
                String songSize;

                if (mb != 0) {
                    songSize = mb + "mb(s)";
                } else {
                    songSize = kb + "kb(s)";
                }

                if (minutes == 0) {
                    songDuration = seconds + " second(s)";
                } else if (seconds < 10) {
                    songDuration = minutes + ":0" + seconds;
                } else {
                    songDuration = String.valueOf(minutes) + ":" + String.valueOf(seconds);
                }

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);


                // Stores column values and the contentUri in a local object
                // that represents the media file.
                mediaList.add(new MyMedia(id, contentUri, thumbnail, name, title, album, artist, dateAdded, songDuration, songSize));
            }
            return mediaList;
        }
    }
}

