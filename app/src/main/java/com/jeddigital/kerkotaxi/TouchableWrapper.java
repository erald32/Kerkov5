package com.jeddigital.kerkotaxi;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by theodhori on 10/2/2016.
 */

public  class TouchableWrapper extends FrameLayout {

    private long lastTouched = 0;
    private static final long SCROLL_TIME = 200L; // 200 Milliseconds, but you can adjust that to your liking
    private UpdateMapAfterUserInteraction updateMapAfterUserInteraction;

    public TouchableWrapper(Context context) {
        super(context);
        // Force the host activity to implement the UpdateMapAfterUserInterection Interface
        try {
            updateMapAfterUserInteraction = (MenuHyreseActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateMapAfterUserInteraction.onUpdateMapOnUserInteraction();
                Log.e("dev", "Action Down");
                break;
            case MotionEvent.ACTION_UP:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateMapAfterUserInteraction.onUpdateMapAfterUserInteraction();
                    }
                }, 600);
                Log.e("dev", "Action Up");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    // Map Activity must implement this interface
    public interface UpdateMapAfterUserInteraction {
        public void onUpdateMapOnUserInteraction();
        public void onUpdateMapAfterUserInteraction();
    }
}
