package com.blockchain.store.playmarket.utilities;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Crypton04 on 02.03.2018.
 */

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private final int Y_BUFFER = 10;
    private RecyclerView mRecyclerView;

    public GestureListener(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // Prevent ViewPager from intercepting touch events as soon as a DOWN is detected.
        // If we don't do this the next MOVE event may trigger the ViewPager to switch
        // tabs before this view can intercept the event.
        mRecyclerView.requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            // Detected a horizontal scroll, prevent the viewpager from switching tabs
            mRecyclerView.requestDisallowInterceptTouchEvent(true);
        } else if (Math.abs(distanceY) > Y_BUFFER) {
            // Detected a vertical scroll of large enough magnitude so allow the the event
            // to propagate to ancestor views to allow vertical scrolling.  Without the buffer
            // a tab swipe would be triggered while holding finger still while glow effect was
            // visible.
            mRecyclerView.requestDisallowInterceptTouchEvent(false);
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }
}
