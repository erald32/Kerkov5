package com.jeddigital.kerkotaxi.CustomUI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Theodhori on 8/8/2016.
 */



public class MyriadProBoldTextView extends TextView {

    public MyriadProBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public MyriadProBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MyriadProBoldTextView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        if (!isInEditMode()) {
            Typeface myTypeface = Typeface.createFromAsset(getContext()
                    .getAssets(), "Fonts/MyriadPro-Bold.otf");
            setTypeface(myTypeface);
        }
    }

}