package com.zsf.toolbox.ui.barrage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;


/**
 * @author EWorld  e-mail:852333743@qq.com
 * 2019/3/8
 */
public class CaptionTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "CaptionTextView";
    private Paint mTxtPaint;
    private Paint mBgPaint;
    private int screenWidth;
    private int screenHeight;
    private int startX;
    private int startY;
    private int middleX;
    private String mContent = "";
    private boolean isStart = false;

    private int index = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public CaptionTextView(Context context) {
        this(context, null);
    }

    public CaptionTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTxtPaint = new Paint();
        mTxtPaint.setAntiAlias(true);
        mTxtPaint.setColor(Color.WHITE);
        mTxtPaint.setTextSize(180);
        mTxtPaint.setStyle(Paint.Style.FILL);

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLACK);
        mBgPaint.setStyle(Paint.Style.FILL);

        setBackgroundColor(Color.BLACK);
    }

    @Override
    public boolean isFocused() {
        //始终返回true
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        middleX = screenWidth / 2;
        startY = screenHeight / 2;
        Log.d(TAG, ">>>>>>> screenWidth = " + screenWidth + " ,screenHeight = " + screenHeight + " ,middleX = " + middleX + " ,startY = " + startY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, ">>>>>> onDraw() mContent = " + mContent);
        float txtWidth = mTxtPaint.measureText(mContent);
        Log.d(TAG, ">>>>>> onDraw() txtWidth = " + txtWidth);
        startX = startX - index;
        if (startX <= -txtWidth) {
            index = 0;
            startX = screenWidth;
        }
        canvas.drawText(mContent, startX, startY, mTxtPaint);

    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public void setTextSize(int txtSize) {
        mTxtPaint.setTextSize(txtSize);
        invalidate();
    }

    public void startMarquee() {
        this.isStart = true;
        this.startX = middleX;
        MyRunnable runnable = new MyRunnable();
        new Thread(runnable).start();

    }

    public void stopMarquee() {
        this.isStart = false;
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (isStart) {
                try {
                    Thread.sleep(600);
                    index = index + 20;
                    mHandler.sendEmptyMessage(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
