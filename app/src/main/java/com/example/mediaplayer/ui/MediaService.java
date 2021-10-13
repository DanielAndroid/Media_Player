package com.example.mediaplayer.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mediaplayer.R;
import com.example.mediaplayer.models.MyMedia;

import java.io.IOException;
import java.util.ArrayList;

public class MediaService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {

   static ArrayList<MyMedia> mediaList;
    public MediaPlayer mediaPlayer;
    Uri currentUri;
    private final IBinder binder = new MediaBinder();
    private String songTitle="";
    private static final int NOTIFY_ID = 1;
    int position;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = intent.getIntExtra("position", 0);
        try {
            playSelectedMedia();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }


    public class MediaBinder extends Binder {
        MediaService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.release();
        stopForeground(true);
        super.onDestroy();
    }

    public void playSelectedMedia() throws IOException {
        currentUri = getMediaUri(position);
        playMedia();
        ControlMediaActivity.setInfo(position);
    }

    public Uri getMediaUri(int position) {
        MyMedia myMedia = mediaList.get(position);
        songTitle = myMedia.getTitle();
        return myMedia.getUri();
    }


    public void playMedia() throws IOException {
        if (mediaPlayer == null) {
            setUpMediaPlayer();
        }
        mediaPlayer.setDataSource(this, currentUri);
        mediaPlayer.prepareAsync();
    }

    public void playNextMedia() throws IOException {
        mediaPlayer.reset();
        if (position != mediaList.size() -1) {
            position++;
        }else {
            position = 0;
        }
        currentUri = getMediaUri(position);
        playMedia();
        ControlMediaActivity.clearViews();
        ControlMediaActivity.setInfo(position);
    }

    public void playPreviousMedia() throws IOException {
        mediaPlayer.reset();
        if (position != 0) {
            position--;
        }else {
            position = mediaList.size() - 1;
        }
        currentUri = getMediaUri(position);
        playMedia();
        ControlMediaActivity.clearViews();
        ControlMediaActivity.setInfo(position);
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }

    public boolean checkIfPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seek(int i) {
         mediaPlayer.seekTo(i);

    }

    public int getDuration() {
        return  mediaPlayer.getDuration();
    }

    public void resetMediaPlayer(){
       mediaPlayer.reset();
    }

    public void setUpMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer)  {
        mediaPlayer.reset();
        if (position != mediaList.size() -1) {
            position++;
            currentUri = getMediaUri(position);
            mediaPlayer.reset();
            try {
                playMedia();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            position = 0;
            currentUri = getMediaUri(position);
            try {
                playMedia();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ControlMediaActivity.clearViews();
        ControlMediaActivity.setInfo(position);

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
//
//        String NOTIFICATION_CHANNEL_ID = "com.example.mediaplayer";
//        String channelName = "My Foreground Service";
//        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//        chan.setLightColor(Color.BLUE);
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert manager != null;
//        manager.createNotificationChannel(chan);
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        Notification notification =
//                new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
//                        .setContentTitle("Now Playing")
//                        .setContentText(songTitle)
//                        .setSmallIcon(R.drawable.play_arrow)
//                        .setContentIntent(pendingIntent)
//                        .setTicker("This is ticker text")
//                        .build();
//
//// Notification ID cannot be 0.
//        startForeground(NOTIFY_ID, notification);
    }

}
