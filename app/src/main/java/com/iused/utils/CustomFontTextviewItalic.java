package com.iused.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Antto on 03/01/2017.
 */

public class CustomFontTextviewItalic extends TextView {

    public CustomFontTextviewItalic(Context context) {
        super(context);
//        applyCustomFont(context);
    }

    public CustomFontTextviewItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/bariolregital.otf"));
//        applyCustomFont(context);
    }

    public CustomFontTextviewItalic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        applyCustomFont(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomFontTextviewItalic(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
