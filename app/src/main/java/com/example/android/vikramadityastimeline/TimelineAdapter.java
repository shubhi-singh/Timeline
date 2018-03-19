package com.example.android.vikramadityastimeline;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vikramadityastimeline.data.TimelineContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by coolio1 on 4/8/17.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewholder> {

    private Cursor mCursor;
    private Context mContext;
     private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    public TimelineAdapter(Cursor cursor, Context context)
    {
        this.mCursor = cursor;
        this.mContext = context;


    }

    @Override
    public TimelineViewholder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        int layoutIdForListItem = R.layout.card_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        TimelineViewholder holder = new TimelineViewholder(view);
        return holder;




    }

    @Override
    public void onBindViewHolder(TimelineViewholder holder, int position)
    {


        new AsyncFunctions().execute(new QueryResult(holder, position));

    }

    @Override
    public int getItemCount()

    {
        return mCursor.getCount();
    }


    public class TimelineViewholder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        public ImageView mThumbnail;
        public PacificoFont mTitle;
        public PacificoFont mDate;

        public ImageButton mDelete;
        public ImageButton mEdit;
        private CardView cardView;
        public TimelineViewholder(View view) {
            super(view);
            mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            mTitle = (PacificoFont) view.findViewById(R.id.title);
            mDate = (PacificoFont) view.findViewById(R.id.date);
            cardView = (CardView) view.findViewById(R.id.card_view);
            mDelete = (ImageButton) view.findViewById(R.id.imageButton2);
            mEdit = (ImageButton) view.findViewById(R.id.imageButton);
            view.setOnClickListener(this);
            mThumbnail.setOnClickListener(this);
            mDelete.setOnClickListener(this);
            mEdit.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            view.setAnimation(buttonClick);
             final int pos = getAdapterPosition();

            if(view.getId() == R.id.thumbnail) {



                Intent intent = new Intent(mContext, InfoActivity.class);
                intent.putExtra("ID", pos);

                mContext.startActivity(intent);



            }

            else  if(view.getId() == R.id.imageButton2)
            {
                 boolean response = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete this memory?\n" +
                        "It looks pretty perfect. Also, any information related to this item will be lost and" +
                        " you will have to add the picture again, if and when you wish to"+
                        " see this again on the Timeline!"
                );

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        mCursor.moveToPosition(pos);
                        long id = mCursor.getLong(mCursor.getColumnIndex(TimelineContract.TimelineEntry._ID));
                        MainActivity.mDb.delete(TimelineContract.TimelineEntry.TABLE_NAME, TimelineContract.TimelineEntry._ID+"="+id, null);
                        swapCursor(MainActivity.getAllCards());
                        dialog.dismiss();


                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }
            else if(view.getId() == R.id.imageButton)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                builder.setTitle("Edit");
                builder.setMessage("Are you sure you want to edit this memory?\n" +
                        "It looks pretty perfect.\n" +
                        "You will have to enter the date, title, and description fields again as" +
                        " all previous information related to this memory will be lost !"

                );

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, EditActivity.class);
                        intent.putExtra("POSITION", pos);

                        mContext.startActivity(intent);
                        swapCursor(MainActivity.getAllCards());

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


            }

            }

        }






    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    private class AsyncFunctions extends AsyncTask<QueryResult, Void, QueryResult>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }
        @Override
        protected QueryResult doInBackground(QueryResult... holder1)
        {

            int position = holder1[0].position;

            if(!mCursor.moveToPosition(position))
                return null;
            byte[] uriInBytes = mCursor.getBlob(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_IMAGE_URI));
            Bitmap image = BitmapFactory.decodeByteArray(uriInBytes, 0, uriInBytes.length);
            String title = mCursor.getString(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_TITLE));
            SimpleDateFormat formatter = new SimpleDateFormat("y/m/d");
            long time = mCursor.getLong(mCursor.getColumnIndex(TimelineContract.TimelineEntry.COLUMN_TIME_STAMP));

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(time));
            String date = formatter.format(calendar.getTime());


            holder1[0].mTitle = title;
            holder1[0].mThumbnail = image;
            holder1[0].mDate = date;

            return holder1[0];

        }

        @Override
        protected void onPostExecute(QueryResult result)
        {
            result.holder.mTitle.setText(result.mTitle);
            result.holder.mThumbnail.setImageBitmap(result.mThumbnail);
            result.holder.mDate.setText(result.mDate);







        }
    }
    private class QueryResult
    {
        public Bitmap mThumbnail;
        public String mTitle;
        public TimelineViewholder holder;
        public int position;
        public String mDate;

        public QueryResult(Bitmap imageView, String textView, String tv2)
        {
            mThumbnail = imageView;
            mTitle = textView;
            mDate = tv2;
        }
        public QueryResult(TimelineViewholder holder, int position)
        {
            this.position = position;
            this.holder = holder;
            mThumbnail = null;
            mTitle = null;
            mDate = null;
        }
    }
}


