package com.jaytechnologies.looper;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import 	android.content.Intent;


public class AudioVideoChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_video_chooser);
        Button audioChoice = findViewById(R.id.audioBtn);
        Button videoChoice = findViewById(R.id.videoBtn);


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
        audioChoice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //does nothing... need audio view

            }
        });

        videoChoice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Viewfinder.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
