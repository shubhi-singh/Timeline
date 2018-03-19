package com.example.android.vikramadityastimeline.data;
import com.example.android.vikramadityastimeline.R;

/**
 * Created by coolio1 on 3/8/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;

import com.example.android.vikramadityastimeline.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static java.security.AccessController.getContext;

public class TimelineDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "timeline.db";
    private static final int DATABASE_VERSION =100;


    public TimelineDbHelper(Context context){

        super(context,DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_TIMELINE_TABLE = "CREATE TABLE " +
                TimelineContract.TimelineEntry.TABLE_NAME + " (" +
                TimelineContract.TimelineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TimelineContract.TimelineEntry.COLUMN_IMAGE_URI + " BLOB NOT NULL, " +
                TimelineContract.TimelineEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TimelineContract.TimelineEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TimelineContract.TimelineEntry.COLUMN_TIME_STAMP + " INTEGER" +
                "); ";
        db.execSQL(SQL_CREATE_TIMELINE_TABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.mContext2.getResources(), R.drawable.nona);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] imageInBytes = stream.toByteArray();
        String title = "This is for you!";
        String des = "Hey Nona! This is Shubhi or as mummy and daddy must have told you, Munmun Didi :P. This app is my" +
                " gift to you, this Raksha Bandhan. I have intended this to be a space where you can " +
                "preserve your memories and accomplishments and look back upon them in glee. Big or small, any memory that is " +
                "worth remembering, add it here ! Just add a picture describing it, and a title, description and date and " +
                "it shall appear with the rest, chronologically" +

                ".You are totally in control of this space, so feel free to add, edit or delete anytime you like." +
                "There is still much left to do, with this app, and I will update it soon." +
                "But till then, I hope you like it !\n"
                +
                "Lots of love!\n"+
                "Shubhi";
        ContentValues cv = new ContentValues();
        cv.put(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI,imageInBytes);
        cv.put(TimelineContract.TimelineEntry.COLUMN_TITLE,title);
        cv.put(TimelineContract.TimelineEntry.COLUMN_DESCRIPTION, des);
        cv.put(TimelineContract.TimelineEntry.COLUMN_TIME_STAMP,0);
        db.insert(TimelineContract.TimelineEntry.TABLE_NAME,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TimelineContract.TimelineEntry.TABLE_NAME);
        onCreate(db);
    }

}
