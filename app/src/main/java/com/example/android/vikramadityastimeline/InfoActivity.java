package com.example.android.vikramadityastimeline;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.vikramadityastimeline.data.TimelineContract;
import com.example.android.vikramadityastimeline.data.TimelineDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.activity_info);

        TimelineDbHelper helper = new TimelineDbHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Intent starter = getIntent();
        if(starter.hasExtra("ID"))
        {
            int id = starter.getIntExtra("ID",0);
            Cursor mCursor;

            mCursor = db.query(TimelineContract.TimelineEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    TimelineContract.TimelineEntry.COLUMN_TIME_STAMP + " ASC"
            );
            mCursor.moveToPosition(id);
            byte[] uriInBytes = mCursor.getBlob(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI));
            Bitmap image = BitmapFactory.decodeByteArray(uriInBytes, 0, uriInBytes.length);
            String title = mCursor.getString(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_TITLE));
            SimpleDateFormat formatter = new SimpleDateFormat("y/m/d");
            long time = mCursor.getLong(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_TIME_STAMP));
            String des =  mCursor.getString(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_DESCRIPTION));

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            String date = formatter.format(calendar.getTime());

            ((ImageView)findViewById(R.id.image_scrolling_top)).setImageBitmap(image);
            ((PacificoFont)findViewById(R.id.title)).setText(title);
            ((PacificoFont)findViewById(R.id.description)).setText(des);




        }

    }
}
