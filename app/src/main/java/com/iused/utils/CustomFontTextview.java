package com.iused.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 4/20/2016.
 */
public class CustomFontTextview extends TextView {
    public CustomFontTextview(Context context) {
        super(context);
//        applyCustomFont(context);
    }

    public CustomFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/bariolreg.otf"));
//        applyCustomFont(context);
    }

    public CustomFontTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        applyCustomFont(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomFontTextview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    private void applyCustomFont(Context context) {
//        Typeface customFont = FontCache.getTypeface("ebrima.ttf", context);
//        setTypeface(customFont);
//    }
}
