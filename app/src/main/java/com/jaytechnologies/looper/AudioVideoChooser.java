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
import android.widget.TextView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.w3c.dom.Text;


public class AudioVideoChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_video_chooser);
        Button audioChoice = findViewById(R.id.audioBtn);
        Button videoChoice = findViewById(R.id.videoBtn);

        //FFmpeg stuff. only run once.
        FFmpeg ffmpeg = FFmpeg.getInstance(getApplicationContext());
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }

        /*//test
        ffmpeg = FFmpeg.getInstance(getApplicationContext());
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(new String[]{"-version"}, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {

                }
                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {

                }
                @Override
                public void onFinish() {}
            });


        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
*/
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
                //does nothing... need audio vieBw

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
