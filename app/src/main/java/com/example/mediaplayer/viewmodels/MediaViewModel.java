package com.example.mediaplayer.viewmodels;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.mediaplayer.models.MyMedia;
import com.example.mediaplayer.models.Repository;

import java.util.ArrayList;

public class MediaViewModel extends ViewModel {
    Repository repository = new Repository();
    boolean isPlaying = false;
    Boolean intentInitialized = false;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getIntentInitialized() {
        return intentInitialized;
    }

    public void setIntentInitialized(Boolean intentInitialized) {
        this.intentInitialized = intentInitialized;
    }


    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

public ArrayList<MyMedia> getMediaList(Context context) {
    return repository.getMedia(context);
}
}
