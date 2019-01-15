package com.jaytechnologies.looper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.Button;
import 	android.content.Intent;
import 	android.net.Uri;
import android.media.MediaPlayer.*;
import android.media.MediaPlayer;
import 	android.content.Context;
import 	android.util.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.database.Cursor;
import android.provider.DocumentsContract;
import android.os.Environment;
import android.content.ContentUris;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Date;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.*;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.h264.H264TrackImpl;
import com.googlecode.mp4parser.authoring.tracks.h265.H265TrackImpl;

public class Viewfinder extends AppCompatActivity {

    Uri videoFileUri;
    Context context;
    SurfaceHolder surfaceHolder;
    //MediaPlayer mp;
    MediaController mc;
    String videoPath = "";
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewfinder);
        context = getApplicationContext();
        mc = new MediaController(this);

        Button btnCamera = (Button) findViewById(R.id.btnViewFinder);
        Button btnVideo3x = findViewById(R.id.video3xBtn);
        //SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        videoView = findViewById(R.id.videoView);
        //surfaceHolder = surfaceView.getHolder();
        //surfaceHolder.addCallback(Viewfinder.this);
        //set looping true (may have to look into other options depending on memory usage??
        //mp = new MediaPlayer();


        // mp.setLooping(true);

        btnCamera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, 0);

            }
        });

        btnVideo3x.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {



                    try {
                    //FileInputStream fi = new FileInputStream(getRealVideoPathFromURI(getContentResolver(),videoFileUri));
                    //Movie mov = MovieCreator.build(fi.getChannel())
                        String path = getRealVideoPathFromURI(getContentResolver(),videoFileUri);
                        Movie mov = MovieCreator.build(path);
                        System.out.println(path);
                        //try overwriting old video
                        Container mp4file = new DefaultMp4Builder().build(mov);
                        FileChannel fc = new RandomAccessFile(String.format("/storage/emulated/0/DCIM/Camera/NEW.mp4"), "rw").getChannel();
                        mp4file.writeContainer(fc);
                        fc.close();

                        File file = new File("/storage/emulated/0/DCIM/Camera/NEW.mp4");
                        if(file.exists()){
                            Log.w("mytag","exists");
                        }
                        else{
                            Log.w("mytag","does not exist");
                        }
                        videoView.stopPlayback();
                        videoView.setVideoPath(file.getAbsolutePath());
                        videoView.start();
                    }catch(Exception e){
                    //TODO: we should make sure that it exists before we allow the button to be clicked or something
                    e.printStackTrace();
                }



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        videoFileUri = data.getData();
        videoView.setVideoURI(videoFileUri);
        videoView.setMediaController(mc);
        videoView.start();
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mc.setAnchorView(videoView);


                mp.setLooping(true);
            }
        });
    }
    public static String getRealVideoPathFromURI(ContentResolver contentResolver,
                                                 Uri contentURI) {
        Cursor cursor = contentResolver.query(contentURI, null, null, null,
                null);
        if (cursor == null)
            return contentURI.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            try {
                return cursor.getString(idx);
            } catch (Exception exception) {
                return null;
            }
        }
    }

}




/*

    public Uri setVideoUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "video" + new Date().getTime() + ".mp4");
        Uri videoUri = Uri.fromFile(file);
        videoPath = file.getAbsolutePath();
        return videoUri;
    }


    */
/**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     *//*

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    */
/**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     *//*

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    */
/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     *//*

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    */
/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     *//*

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    */
/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     *//*

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}*/
