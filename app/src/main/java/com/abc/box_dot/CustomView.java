package com.abc.box_dot;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class CustomView extends View {

    public static final float SIZE = 5;

    public static final String TAG = "Custom View";
    private ArrayList<Dots> dots = new ArrayList<>();
    private ArrayList<Dots> dot_loc = new ArrayList<>();
    private boolean isPathStarted = false;
    private float mTouchTolerance;
    private int color_bg = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
    private int color = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null);
    Paint mPaint, paint_line;
    Canvas mCanvas;
    Path mPath;
    float radius = 25;
    float dist = 795 / (SIZE - 1);
    float start_x, start_y;

    public CustomView(Context context) {
        super(context);

        float x = radius;
        float y = radius;
        for (int i = 0; i < SIZE; i++, y += dist) {
            for (int j = 0; j < SIZE; j++, x += dist) {
                dots.add(new Dots(x, y));
            }
            x = radius;
        }
        mPath = new Path();
        mPaint = new Paint();
        mCanvas = new Canvas();
        paint_line = new Paint();
        mPaint.setColor(color);
        mPaint.setStrokeWidth(1f);
        paint_line.setColor(color);
        paint_line.setStrokeWidth(10f);
        paint_line.setStrokeJoin(Paint.Join.ROUND);
        setWillNotDraw(false);

    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        float x = radius;
        float y = radius;
        float dist = 795 / (SIZE - 1);
        for (int i = 0; i < SIZE; i++, y += dist) {
            for (int j = 0; j < SIZE; j++, x += dist) {
                dots.add(new Dots(x, y));
            }
            x = radius;
        }
        mPath = new Path();
        mPaint = new Paint();
        paint_line = new Paint();
        mCanvas = new Canvas();
        mPaint.setColor(color_bg);
        mPaint.setStrokeWidth(10f);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        paint_line.setColor(color);
        paint_line.setStrokeWidth(10f);
        paint_line.setStrokeJoin(Paint.Join.ROUND);
        mTouchTolerance = 100;
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "value in ondraw :" + start_y);
        canvas.drawPath(mPath, paint_line);
        for (Dots mDots : dots) {
            Log.d(TAG, "dot position" + mDots.getY());
            canvas.drawCircle(mDots.getX(), mDots.getY(), radius, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                for (Dots mDots : dots) {
                    if (x > (mDots.getX() - mTouchTolerance) && x < (mDots.getX() + mTouchTolerance)) {
                        if (y > (mDots.getY() - mTouchTolerance) && y < (mDots.getY() + mTouchTolerance)) {
                            isPathStarted = true;
                            start_x = mDots.getX();
                            start_y = mDots.getY();
                            Log.d(TAG, "begins at x-" + start_x);
                            Log.d(TAG, "begins at y-" + start_y);
                            mPath.moveTo(start_x, start_y);

                        }
                    } else isPathStarted = false;
                }
                break;

            case MotionEvent.ACTION_UP:

                touch_up(x, y);
                break;

            default:
                super.onTouchEvent(event);

        }
        return true;
    }

    private void touch_up(float x, float y) {

        if (isPathStarted) {
            for (Dots first : dots) {
                if (x > (first.getX() - mTouchTolerance) && x < (first.getX() + mTouchTolerance) && (y > (first.getY() - mTouchTolerance) && y < (first.getY() + mTouchTolerance)))
                    if (Math.sqrt((first.getX() - start_x) * (first.getX() - start_x) + (first.getY() - start_y) * (first.getY() - start_y)) <= dist) {
                        Log.d(TAG, "on draw called" + x);
                        Log.d(TAG, "begins at y:" + y);
                        mPath.lineTo(x, y);

                        break;

                    }
            }invalidate();
        }
    }
}



