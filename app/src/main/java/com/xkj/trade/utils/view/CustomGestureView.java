package com.xkj.trade.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.xkj.trade.R;
import com.xkj.trade.utils.SystemUtil;

/**
 * Created by huangsc on 2016-12-16.
 * TODO:
 */

public class CustomGestureView extends View {
    Paint mPaint;
    private int i = 5;
    private String TAG = SystemUtil.getTAG(this);
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    public CustomGestureView(Context context) {
        this(context, null);
    }

    public CustomGestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mGestureDetector = new GestureDetector(context, new MyGestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() <= 1) {
            return mGestureDetector.onTouchEvent(event);
        } else {
            return mScaleGestureDetector.onTouchEvent(event);
        }
    }

    public CustomGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.red));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        i++;
        canvas.drawLine(0, i, getWidth(), i, mPaint);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "onScroll: ");
            invalidate();
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i(TAG, "onFling: velocityX  " + velocityX);
            return false;
        }


        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, "onDown: ");
            return true;
        }


    }

    private class MyScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i(TAG, "onScale: ");
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            Log.i(TAG, "onScaleBegin: ");
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.i(TAG, "onScaleEnd: ");
        }
    }
}
