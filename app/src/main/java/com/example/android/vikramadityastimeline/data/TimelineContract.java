package com.example.android.vikramadityastimeline.data;

/**
 * Created by coolio1 on 3/8/17.
 */

import android.provider.BaseColumns;

public class TimelineContract {
    private TimelineContract() {}

    public static final class TimelineEntry implements BaseColumns{
        public static final String TABLE_NAME = "timeline";
        public static final String COLUMN_IMAGE_URI = "uri";
        public static final String COLUMN_TITLE = "imageTitle";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIME_STAMP = "date";

    }
}

