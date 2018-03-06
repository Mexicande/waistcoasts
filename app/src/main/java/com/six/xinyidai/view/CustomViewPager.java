package com.six.xinyidai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lihuabin on 2016/10/10.
 */
    public class CustomViewPager extends ViewPager {

        private boolean isCanScroll = true;

        public CustomViewPager(Context context) {
            super(context);
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public void setScanScroll(boolean isCanScroll) {
            this.isCanScroll = isCanScroll;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return this.isCanScroll && super.onTouchEvent(event);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return this.isCanScroll && super.onInterceptTouchEvent(event);
        }
    }


