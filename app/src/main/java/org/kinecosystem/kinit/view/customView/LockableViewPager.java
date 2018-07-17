package org.kinecosystem.kinit.view.customView;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class LockableViewPager extends ViewPager {

    public static final int SWIPE_DURATION = 500;
    private boolean swipeable;

    public LockableViewPager(Context context) {
        super(context);
    }

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScroll(); // original scroll is too fast. need to override it
        swipeable = false;
    }

    public void setCurrentItem(int item, boolean smoothScroll, TransitionCompletedListener listener) {
        addOnPageChangeListener(new OnPageChangeListener() {
            private boolean isPageChanged = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                isPageChanged = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && isPageChanged) {
                    removeOnPageChangeListener(this);
                    listener.onTransitionCompleted();
                }
            }
        });
        setCurrentItem(item, smoothScroll);
    }

    private void setScroll() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new SlowScroll(getContext()));
        } catch (Exception e) {
            Log.d("LockableViewPager", "Exception was thrown + " + e.getMessage());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeable) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (swipeable) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void enableSwipe(boolean enable) {
        swipeable = enable;
    }

    public interface TransitionCompletedListener {
        void onTransitionCompleted();
    }

    public class SlowScroll extends Scroller {

        SlowScroll(Context context) {
            super(context, new AccelerateDecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, SWIPE_DURATION);
        }
    }
}
