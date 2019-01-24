package com.jaytechnologies.looper;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import 	android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadOrRecordVideo extends AppCompatActivity {
    Uri videoFileUri;
    Button btnRecord;
    Button btnChoose;
    int SELECT_VIDEO = 1;
    int TAKE_VIDEO = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_record_video);

        btnChoose = findViewById(R.id.btnChoose);
        btnRecord = findViewById(R.id.btnRecord);


        btnRecord.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, TAKE_VIDEO);

            }
        });



        btnChoose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_VIDEO);

            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_VIDEO) {
            videoFileUri = data.getData();

            String path = Common.getRealVideoPathFromURI(getContentResolver(), videoFileUri);

            //open the video view and send this path
        }
        if(requestCode == SELECT_VIDEO){
            String path = Common.getRealVideoPathFromURI(getContentResolver(),data.getData());

            //open the video view and send this path
        }

    }


}
