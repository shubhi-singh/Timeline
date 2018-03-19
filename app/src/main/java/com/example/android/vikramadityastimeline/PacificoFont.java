package com.example.android.vikramadityastimeline;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by coolio1 on 5/8/17.
 */

public class PacificoFont extends TextView {
    public PacificoFont(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf"));
    }
}