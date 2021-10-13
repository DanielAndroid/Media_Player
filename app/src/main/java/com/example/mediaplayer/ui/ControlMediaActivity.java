package com.example.mediaplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.mediaplayer.R;
import com.example.mediaplayer.models.MyMedia;
import com.example.mediaplayer.viewmodels.MediaViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ControlMediaActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
   private static int position;
   private static int currentPosition;
   Intent serviceIntent;
   MediaService mediaService;
   MyMediaController myMediaController;
   MediaViewModel mediaViewModel;
  static Boolean isPlaying = false;
  static ArrayList<MyMedia> mediaList;
   Boolean isBound;
    Handler handler;

    private static  TextView songTextView, artistTextView, albumTextView, sizeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_media);




        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        albumTextView = findViewById(R.id.album_info);
        artistTextView = findViewById(R.id.artist_info);
        sizeTextView = findViewById(R.id.size_info);
        songTextView = findViewById(R.id.song_info);

        handler = new Handler();


        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        MediaService.mediaList = mediaViewModel.getMediaList(this);

        mediaList = mediaViewModel.getMediaList(this);

         position = getIntent().getIntExtra("position", 0);

         serviceIntent = new Intent(ControlMediaActivity.this, MediaService.class);
        }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MediaService.class);
        startMediaService();
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        isBound = false;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaService.MediaBinder binder = (MediaService.MediaBinder) service;
            mediaService = binder.getService();
            isBound = true;
            setupMediaController();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    public  void setupMediaController()  {
        myMediaController = new MyMediaController(ControlMediaActivity.this);
        myMediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaService.playNextMedia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myMediaController.show(0);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaService.playPreviousMedia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myMediaController.show(0);

            }
        });
        myMediaController.setMediaPlayer(ControlMediaActivity.this);
        myMediaController.setAnchorView(findViewById(R.id.imageView));
        setHandler(handler);
    }


    public static void setInfo(int position) {
        MyMedia myMedia = mediaList.get(position);
        String song = myMedia.getNames();
        songTextView.append("  " + song);
        String artist = myMedia.getArtist();
        artistTextView.append("  " + artist);
        String album = myMedia.getAlbum();
        albumTextView.append("  " + album);
        String size = myMedia.getSize();
        sizeTextView.append("  " + size);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void clearViews() {
        songTextView.setText(R.string.song);
        albumTextView.setText(R.string.album);
        artistTextView.setText(R.string.artist);
        sizeTextView.setText(R.string.size);
    }

    @Override
    public void start() {
        if(mediaService.checkIfPlaying()) {
          pause();
        } else {
            mediaService.mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
            mediaService.mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        if(mediaService != null && isBound && mediaService.checkIfPlaying() ) {
            return mediaService.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public int getCurrentPosition() {
        if(mediaService != null && isBound && mediaService.checkIfPlaying() ) {
            return mediaService.getPosition();
        } else {
            return 0;
        }
    }

    @Override
    public void seekTo(int i) {
        if(mediaService != null && isBound && mediaService.checkIfPlaying()) {
            mediaService.seek(i);
        }
    }

    @Override
    public boolean isPlaying() {
        if(mediaService != null && isBound) {
            return mediaService.checkIfPlaying();
        }else {
            return false;
        }
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void startMediaService() {
        if (!isPlaying) {
            serviceIntent.putExtra("position", position);
            startService(serviceIntent);
            isPlaying = true;
        } else {
            if (currentPosition != position) {
                stopService(serviceIntent);
                serviceIntent.putExtra("position", position);
                startService(serviceIntent);
                currentPosition = position;
            }
        }

    }

    public void setHandler(Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                myMediaController.setEnabled(true);
                myMediaController.show();
            }
        });
    }
}
