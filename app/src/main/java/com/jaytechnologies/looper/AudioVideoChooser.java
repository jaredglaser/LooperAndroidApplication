package com.jaytechnologies.looper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import 	android.content.Intent;


public class AudioVideoChooser extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_video_chooser);
        Button uploadChoice = findViewById(R.id.uploadBtn);
        Button recordChoice = findViewById(R.id.recordBtn);



        //TODO: BAD BAD BAD implementation... if they say no then we crash
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(AudioVideoChooser.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    2);


        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AudioVideoChooser.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    3);
        }


        //if they choose audio/video, direct them to the view
        recordChoice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, Request.RECORD_VIDEO.ordinal());

            }
        });

        uploadChoice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //Intent myIntent = new Intent(view.getContext(), Viewfinder.class);
                //startActivityForResult(myIntent, 0);
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent, Request.UPLOAD_VIDEO.ordinal());
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Request.RECORD_VIDEO.ordinal()) {
                Uri videoFileUri = data.getData();

                String path = Common.getRealVideoPathFromURI(getContentResolver(), videoFileUri);
                Log.w(Common.tag, path);


                //open the video view and send this path
            }
            if (requestCode == Request.UPLOAD_VIDEO.ordinal()) {
                String path = Common.getRealVideoPathFromURI(getContentResolver(), data.getData());
                Log.w(Common.tag, path);
                //open the video view and send this path
            }
        }
    }
}
