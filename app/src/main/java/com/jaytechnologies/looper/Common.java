package com.jaytechnologies.looper;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Common {
    public static String tag = "jared";
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

enum Request{
    RECORD_VIDEO,UPLOAD_VIDEO;
}
