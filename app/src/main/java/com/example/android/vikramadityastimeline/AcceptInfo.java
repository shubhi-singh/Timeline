package com.example.android.vikramadityastimeline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

public class AcceptInfo extends AppCompatActivity {

    private Button mButton;
    private static Button mButton2;
    private static Context mContext;
    private static long mTimeInMilli;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_info);
        mButton = (Button) findViewById(R.id.new_button);
        Typeface copperplateGothicLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Pacifico.ttf");
        mButton.setTypeface(copperplateGothicLight);
        mButton2 = (Button) findViewById(R.id.button4);
        (mButton2).setTypeface(copperplateGothicLight);
        mContext = getApplicationContext();
        mTimeInMilli = 0;
        Intent starter = getIntent();
        if (starter.hasExtra("URI")) {
            Uri sent = starter.getParcelableExtra("URI");
            ((ImageView) findViewById(R.id.iv_display)).setTag(sent);
            ((ImageView) findViewById(R.id.iv_display)).setImageURI(sent);


        }
        mButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                mButton.setAnimation(buttonClick);
                String uriInString = (((ImageView) findViewById(R.id.iv_display)).getTag()).toString();
                String title = ((EditText) findViewById(R.id.et_title)).getText().toString();
                String description = ((EditText)findViewById(R.id.et_description)).getText().toString();

                Intent intent = new Intent();
                intent.putExtra("URI_IN_STRING", uriInString);
                intent.putExtra("TITLE", title);
                intent.putExtra("DESCRIPTION", description);
                intent.putExtra("TIME_IN_MILLI",mTimeInMilli);
                setResult(RESULT_OK, intent);

                Toast.makeText(getApplicationContext(), "Added to your Timeline!",
                        Toast.LENGTH_LONG).show();
                AcceptInfo.this.finish();
            }
        });
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
