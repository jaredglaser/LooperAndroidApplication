package com.jaytechnologies.looper;

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
