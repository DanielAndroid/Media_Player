package com.example.mediaplayer.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.Toast;


import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.MediaAdapter;
import com.example.mediaplayer.databinding.ActivityMainBinding;
import com.example.mediaplayer.models.MyMedia;
import com.example.mediaplayer.viewmodels.MediaViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaAdapter.OnMediaItemClickListener {

    MediaAdapter mediaAdapter;
    ArrayList<MyMedia> mediaList;
    MediaViewModel mediaViewModel;
    RecyclerView recyclerView;

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    if(mediaList == null) {
                        mediaList = mediaViewModel.getMediaList(this);
                    }
                    recyclerView.setAdapter(new MediaAdapter(this, mediaList, this));
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    showAlertDialog();
                }
            });

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            if(mediaList == null) {
                mediaList = mediaViewModel.getMediaList(this);
            }
            recyclerView.setAdapter(new MediaAdapter(this, mediaList, this));

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            showAlertDialog();

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            makePermissionRequest();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaViewModel = new ViewModelProvider(this).get(MediaViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void makePermissionRequest() {
        requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    // is called if the permission is not given.
    public void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app needs you to allow a permission in order to function.Will you allow it?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        makePermissionRequest();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onMediaItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ControlMediaActivity.class);
        intent.putExtra("position", position);

        startActivity(intent);
    }

}
