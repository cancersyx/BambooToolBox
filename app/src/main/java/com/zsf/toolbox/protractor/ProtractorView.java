package com.zsf.toolbox.protractor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by EWorld
 * 2022/5/31
 */
public class ProtractorView extends View {
    private static final String TAG = "ProtractorView";
    private Paint mPaint;
    private Paint mTxtPaint;
    private Paint mDashPaint;

    private Paint mDegreePaint;
    private int longScale = 20;//长刻度线
    private int shortScale = 10;//短刻度线

    private int screenWidth;
    private int screenHeight;

    public ProtractorView(Context context) {
        this(context, null);
    }

    public ProtractorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProtractorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        mTxtPaint = new Paint();
        mTxtPaint.setColor(Color.BLACK);
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setStyle(Paint.Style.STROKE);
        mTxtPaint.setStrokeWidth(2);
        mTxtPaint.setTextSize(26);

        mDashPaint = new Paint();
        mDashPaint.setColor(Color.BLACK);
        mDashPaint.setAntiAlias(true);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(2);
        mDashPaint.setDither(true);
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));//绘制长度为10的实线后再绘制长度为10的空白，0位间隔

        mDegreePaint = new Paint();
        mDegreePaint.setColor(Color.BLACK);
        mDegreePaint.setAntiAlias(true);
        mDegreePaint.setStyle(Paint.Style.STROKE);
        mDegreePaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG, ">>>>> screenWidth = " + screenWidth + " ,screenHeight = " + screenHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = screenHeight - 40;//顶着屏幕边缘不好看,减去个40
        canvas.drawCircle(screenWidth / 2, screenHeight, radius, mPaint);
        canvas.drawCircle(screenWidth / 2, screenHeight, 80, mPaint);//画个辅助半圆,80是随意给的一个半径

        canvas.save();
        //画刻度线
        for (int i = 0; i < 360; i++) {
            if (i % 10 == 0) {
                mDegreePaint.setStrokeWidth(5);
                canvas.drawLine(screenWidth / 2, screenHeight - radius, screenWidth / 2, screenHeight - radius + longScale, mDegreePaint);
            } else {
                mDegreePaint.setStrokeWidth(3);
                //显示短的刻度线
                canvas.drawLine(screenWidth / 2, screenHeight - radius, screenWidth / 2, screenHeight - radius + shortScale, mDegreePaint);
            }
            canvas.rotate(1, screenWidth / 2, screenHeight);
        }
        canvas.restore();

        //--------------------------0度-------------------------------------------------------------------START
        canvas.drawLine(screenWidth / 2, screenHeight, screenWidth / 2 - radius, screenHeight, mPaint);
        float widthScaleTxt = mTxtPaint.measureText("0");
        canvas.drawText("0", screenWidth / 2 - radius - widthScaleTxt, screenHeight, mTxtPaint);
        //---------------------------0度------------------------------------------------------------------END

        //--------------------------90度-------------------------------------------------------------------START
        canvas.drawLine(screenWidth / 2, screenHeight, screenWidth / 2, 40, mPaint);
        widthScaleTxt = mTxtPaint.measureText("90");
        canvas.drawText("90", screenWidth / 2 - widthScaleTxt / 2, 40, mTxtPaint);
        //---------------------------90度------------------------------------------------------------------END

        //--------------------------60度-------------------------------------------------------------------START
        double endX60 = screenWidth / 2 - Math.cos(Math.toRadians(60)) * radius;
        double endY60 = screenHeight - Math.sin(Math.toRadians(60)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX60, (float) endY60, mDashPaint);
        canvas.drawText("60", (float) endX60 - widthScaleTxt, (float) endY60, mTxtPaint);
        //--------------------------60度-------------------------------------------------------------------END

        //--------------------------45度-------------------------------------------------------------------START
        double endX45 = screenWidth / 2 - Math.cos(Math.toRadians(45)) * radius;
        double endY45 = screenHeight - Math.sin(Math.toRadians(45)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX45, (float) endY45, mDashPaint);
        canvas.drawText("45", (float) endX45 - widthScaleTxt, (float) endY45, mTxtPaint);
        //--------------------------45度-------------------------------------------------------------------END

        //--------------------------30度-------------------------------------------------------------------START
        double endX30 = screenWidth / 2 - Math.cos(Math.toRadians(30)) * radius;
        double endY30 = screenHeight - Math.sin(Math.toRadians(30)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX30, (float) endY30, mDashPaint);
        canvas.drawText("30", (float) endX30 - widthScaleTxt, (float) endY30, mTxtPaint);
        //--------------------------30度-------------------------------------------------------------------END

        widthScaleTxt = mDashPaint.measureText("120");
        //--------------------------120度-------------------------------------------------------------------START
        double endX120 = screenWidth / 2 - Math.cos(Math.toRadians(120)) * radius;
        double endY120 = screenHeight - Math.sin(Math.toRadians(120)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX120, (float) endY120, mDashPaint);
        canvas.drawText("120", (float) endX120 + widthScaleTxt / 2, (float) endY120, mTxtPaint);
        //--------------------------120度-------------------------------------------------------------------END

        //--------------------------135度-------------------------------------------------------------------START
        double endX135 = screenWidth / 2 - Math.cos(Math.toRadians(135)) * radius;
        double endY135 = screenHeight - Math.sin(Math.toRadians(135)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX135, (float) endY135, mDashPaint);
        canvas.drawText("135", (float) endX135 + widthScaleTxt / 2, (float) endY135, mTxtPaint);
        //--------------------------135度-------------------------------------------------------------------END

        //--------------------------150度-------------------------------------------------------------------START
        double endX150 = screenWidth / 2 - Math.cos(Math.toRadians(150)) * radius;
        double endY150 = screenHeight - Math.sin(Math.toRadians(150)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX150, (float) endY150, mDashPaint);
        canvas.drawText("150", (float) endX150 + widthScaleTxt / 2, (float) endY150, mTxtPaint);
        //--------------------------150度-------------------------------------------------------------------END


        //--------------------------180度-------------------------------------------------------------------START
        double endX180 = screenWidth / 2 - Math.cos(Math.toRadians(180)) * radius;
        double endY180 = screenHeight - Math.sin(Math.toRadians(180)) * radius;
        canvas.drawLine(screenWidth / 2, screenHeight, (float) endX180, (float) endY180, mDashPaint);
        canvas.drawText("180", (float) endX180 + widthScaleTxt / 2, (float) endY180, mTxtPaint);
        //--------------------------180度-------------------------------------------------------------------END

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,">>>>>> onTouch");
        return super.onTouchEvent(event);
    }
}
