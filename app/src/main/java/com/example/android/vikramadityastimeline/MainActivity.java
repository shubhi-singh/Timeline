package com.example.android.vikramadityastimeline;

import android.content.ContentValues;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vikramadityastimeline.data.TimelineContract;
import com.example.android.vikramadityastimeline.data.TimelineDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
   ;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int SEND_DATA_REQUEST = 2;
   public static final int SEND_EDITED_DATA = 3;
    public  static SQLiteDatabase mDb;
    LinearLayoutManager layoutManager;
    public  TimelineAdapter mAdapter;
    private RecyclerView mRv;
    private Cursor mCursor;
    public  Context mContext;
    public static Context mContext2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mContext2 = mContext;





        TimelineDbHelper helper = new TimelineDbHelper(this);
        mDb = helper.getWritableDatabase();
        mRv = (RecyclerView) findViewById(R.id.card_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        mRv.setLayoutManager(layoutManager);
        mRv.setHasFixedSize(true);


        // Create a DB helper (this will create the DB if run for the first time)


        // Get all guest info from the database and save in a cursor
        mCursor = getAllCards();


        // Create an adapter for that cursor to display the data
        mAdapter = new TimelineAdapter(mCursor, this);

        // Link the adapter to the RecyclerView
        mRv.setAdapter(mAdapter);





    }
   @Override
   protected void onStart()
   {
       super.onStart();

   }

    public void addToTimeline(View view)
    {
        openGalleryActivity();
    }
    private void openGalleryActivity( )
    {

        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();

            startInfoActivity(fullPhotoUri);

        }
        else if(requestCode == SEND_DATA_REQUEST && resultCode == RESULT_OK)
        {
            String uriInString = data.getStringExtra("URI_IN_STRING");
            String title = data.getStringExtra("TITLE");
            String description = data.getStringExtra("DESCRIPTION");
            long timeInMilli = data.getLongExtra("TIME_IN_MILLI",0);
            addToDataBase(uriInString, title, description,timeInMilli);
        }


        return;
    }

    public void startInfoActivity(Uri uri)
    {
        Intent intent = new Intent(MainActivity.this, AcceptInfo.class);
        intent.putExtra("URI", uri);
        startActivityForResult(intent, SEND_DATA_REQUEST);


    }



    public void addToDataBase(String uriInString, String title, String description, long timeInMilli)
    {
        addCard(uriInString, title, description, timeInMilli);
        mAdapter.swapCursor(getAllCards());

    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void addCard(String uriInString, String title, String description, long timeInMilli)
    {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(uriInString));
        }

        catch (IOException e) {}
        bitmap = getResizedBitmap(bitmap, 500);
        byte[] uriInBytes = null;




            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            uriInBytes = stream.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI, uriInBytes);
        cv.put(TimelineContract.TimelineEntry.COLUMN_TITLE, title);
        cv.put(TimelineContract.TimelineEntry.COLUMN_DESCRIPTION, description);
        cv.put(TimelineContract.TimelineEntry.COLUMN_TIME_STAMP, timeInMilli);
        mDb.insert(TimelineContract.TimelineEntry.TABLE_NAME, null, cv);
    }

       static public Cursor getAllCards()
    {
        return mDb.query(
                TimelineContract.TimelineEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TimelineContract.TimelineEntry.COLUMN_TIME_STAMP + " ASC"

        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */
            case R.id.action_refresh:
                // COMPLETED (14) Pass in this as the ListItemClickListener to the GreenAdapter constructor
                layoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
        }

        return super.onOptionsItemSelected(item);
    }




}
