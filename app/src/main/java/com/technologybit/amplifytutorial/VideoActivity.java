package com.technologybit.amplifytutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.StorageAccessLevel;
import com.amplifyframework.storage.options.StorageUploadFileOptions;
import com.amplifyframework.storage.result.StorageUploadFileResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class VideoActivity extends AppCompatActivity {

    private final int CODE_VIDEO = 1;
    private VideoView videoView;
    private File videoFile = null;
    Button uploadButton, selectBtn;
    String[] fileArrayName;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        uploadButton = findViewById(R.id.uploadBtn);
        selectBtn = findViewById(R.id.selectBtn);

        uploadButton.setEnabled(false);
        selectBtn.setEnabled(false);

        videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.INVISIBLE);

        uploadButton.setText("Upload");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        enableButton();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            enableButton();
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                100);
            }
        }
    }

    public void enableButton() {
        selectBtn.setEnabled(true);
    }

    public void btnSelectVideo(View view) {
        Log.i("Buttons", "Select Video Button");

        startActivityForResult(Intent.createChooser(new Intent()
        .setAction(Intent.ACTION_GET_CONTENT)
        .setType("video/mp4"),
        "Select Video"),
                CODE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_VIDEO && resultCode == RESULT_OK) {
            assert data != null;
            Uri videoUri = data.getData();

            if (videoUri != null) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            }

            File fileUtils = null;
            try {
                fileUtils = FileUtils.getFileFromUri(getBaseContext(), videoUri);
            } catch (Exception e) {
                e.printStackTrace();
            }

            assert videoUri != null;
            Log.i("VideoUri", videoUri.toString());
            assert fileUtils != null;

            Log.i("VideoUri", fileUtils.toString());
            fileArrayName = fileUtils.toString().split("/");
            fileName = fileArrayName[fileArrayName.length - 1];

            videoFile = fileUtils;
            uploadButton.setEnabled(true);
            videoView.setVisibility(View.VISIBLE);

        }

    }

    public void btnUpload(View view) {
        uploadButton.setText("Uploading.....");
        if (videoFile != null) {
            uploadFile(videoFile, fileName);
        }
    }

    private void uploadFile(File filePath, String fileName) {
        StorageUploadFileOptions options = StorageUploadFileOptions.builder()
                .accessLevel(StorageAccessLevel.PUBLIC)
                .build();

        Amplify.Storage.uploadFile(
                fileName,
                filePath,
                options,
                this::successUpload,
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    public void successUpload(StorageUploadFileResult result) {
        Intent i = new Intent(getApplicationContext(),ViewVideoActivity.class);
        i.putExtra("FileName", fileName);
        startActivity(i);
    }

}