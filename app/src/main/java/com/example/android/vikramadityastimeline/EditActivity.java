package com.example.android.vikramadityastimeline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.android.vikramadityastimeline.data.TimelineContract;

public class EditActivity extends AppCompatActivity {

    private Button mButton;
    private static Button mButton2;
    private static Context mContext;
    private static long mTimeInMilli;
    private Cursor mCursor;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mButton = (Button) findViewById(R.id.new_button);
        Typeface copperplateGothicLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Pacifico.ttf");
        mButton.setTypeface(copperplateGothicLight);
        mButton2 = (Button) findViewById(R.id.button4);
        (mButton2).setTypeface(copperplateGothicLight);
        mContext = getApplicationContext();
        mTimeInMilli = 0;
       final  Intent starter = getIntent();
        int pos = starter.getIntExtra("POSITION",0);
         mCursor = MainActivity.mDb.query(TimelineContract.TimelineEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TimelineContract.TimelineEntry.COLUMN_TIME_STAMP + " ASC"
        );

        mCursor.moveToPosition(pos);

        byte[] uriInBytes = mCursor.getBlob(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI));
        Bitmap bitmap= BitmapFactory.decodeByteArray(uriInBytes, 0, uriInBytes.length);
        ((ImageView)findViewById(R.id.iv_display)).setImageBitmap(bitmap);
        mButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
               mButton.setAnimation(buttonClick);
                int pos =1;
                if (starter.hasExtra("POSITION")) {

                    pos = starter.getIntExtra("POSITION",0);

                }

                String title = ((EditText) findViewById(R.id.et_title)).getText().toString();
                String description = ((EditText)findViewById(R.id.et_description)).getText().toString();
                updateDataBase(pos,title,description,mTimeInMilli);


                EditActivity.this.finish();
            }
        });
    }

     public void updateDataBase(int pos, String title, String description, long timeInMilli)
    {
        updateCard(pos, title, description, timeInMilli);


    }
     public void updateCard(int pos, String title, String description, long timeInMilli)
    {
        mCursor.moveToPosition(pos);

        byte[] uriInBytes = mCursor.getBlob(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI));
        long id = mCursor.getLong(mCursor.getColumnIndex(TimelineContract.TimelineEntry._ID));

        ContentValues cv = new ContentValues();
        cv.put(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI, uriInBytes);
        cv.put(TimelineContract.TimelineEntry.COLUMN_TITLE, title);
        cv.put(TimelineContract.TimelineEntry.COLUMN_DESCRIPTION, description);
        cv.put(TimelineContract.TimelineEntry.COLUMN_TIME_STAMP, timeInMilli);
        MainActivity.mDb.update(TimelineContract.TimelineEntry.TABLE_NAME,
                cv,
                TimelineContract.TimelineEntry._ID+"="+id,
                null);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public  static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            try {
                Date truc = new SimpleDateFormat("y/m/d").parse(year + "/" + month + "/" + day);
                mTimeInMilli = truc.getTime();
                mButton2.setText(year + "/" + month + "/" + day);
                mButton2.setTextSize(20);

            }catch (ParseException e) { }
        }
    }


}
