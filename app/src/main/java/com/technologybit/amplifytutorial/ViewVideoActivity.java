package com.technologybit.amplifytutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageException;
import com.amplifyframework.storage.StorageItem;
import com.amplifyframework.storage.result.StorageGetUrlResult;

import java.io.File;
import java.net.URL;

public class ViewVideoActivity extends AppCompatActivity {

    String fileName;
    VideoView videoViewShow;
    MediaController mediaController;
    String url;
    TextView tvMessage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        fileName = null;

        videoViewShow = findViewById(R.id.videoViewShow);
        tvMessage = findViewById(R.id.tvMessage);
        videoViewShow.setVisibility(View.INVISIBLE);

        tvMessage.setText("Please Upload A Video!!!");

        Intent intent = getIntent();
        if (intent.getStringExtra("FileName") != null) {
            fileName = intent.getStringExtra("FileName");
        }

        if (fileName != null && url == null) {
            Log.i("MyAmplifyApp", "This If Condition Is Run");
            Log.i("MyAmplifyApp", fileName);
            Amplify.Storage.getUrl(
                    fileName,
                    this::urlSuccess,
                    this::urlFailed
            );
            url = null;
        } else if (url != null) {
            playVideo(this.url);
        } else {
            Log.i("MyAmplifyApp", "None");
        }

    }

    @SuppressLint("SetTextI18n")
    public void urlSuccess(StorageGetUrlResult result) {
        videoViewShow.setVisibility(View.VISIBLE);
        tvMessage.setText("Please Wait Playing The Video....");
        String url = result.getUrl().toString();
        Log.i("MyAmplifyApp", url);
        Log.i("MyAmplifyApp", "Url Success");
        this.url = url;
        playVideo(url);
    }

    public void playVideo(String videoPath) {

        Uri video = Uri.parse(videoPath);
        videoViewShow.setVideoURI(video);

        mediaController = new MediaController(this);
        videoViewShow.setMediaController(mediaController);
        mediaController.setAnchorView(videoViewShow);
        videoViewShow.start();

    }

    public void urlFailed(StorageException error) {
        Log.i("MyAmplify", error.toString());
        runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(getBaseContext(), error.getMessage(),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        tvMessage.setText(error.getMessage());
    }

    public void btnAddNewVideo(View view) {
        Log.i("Buttons", "Add New Video Button Clicked");
        Intent i = new Intent(getApplicationContext(),VideoActivity.class);
        startActivity(i);
    }

}