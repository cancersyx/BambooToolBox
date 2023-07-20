package com.zsf.toolbox.ui.decibel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.zsf.toolbox.R;

import java.text.DecimalFormat;

/**
 * @author EWorld  e-mail:852333743@qq.com
 * 2020/1/13
 */
public class DialPlateView extends View {
    private static final int DEFAULT_SIDE = 200;
    private static final int DEFAULT_NUM = 0;
    private static final float DEFAULT_ARC_DEGREES = 270F;
    private static final float DEFAULT_DEGREES = 135f;
    private static final float DEFAULT_SCALE_VALUE = 140f;
    private static final String DEFAULT_SHOW_DB = "0.00";
    private float mRotateValues;
    private Paint mPaint;
    private Rect mDrawBitmapRect;
    private Bitmap mDialBitmap;
    private Bitmap mPointerBitmap;
    private int mWidth;
    private int mHeight;
    private boolean isInited = false;
    private Matrix mMatrix;
    private boolean drawControlLine;
    private String showDB = DEFAULT_SHOW_DB;//显示分贝内容


    public DialPlateView(Context context) {
        this(context, null);
    }

    public DialPlateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialPlateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDialBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_dial);
        mPointerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_needle);
        mPointerBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(50);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mDrawBitmapRect = new Rect();
        mRotateValues = DEFAULT_ARC_DEGREES / DEFAULT_SCALE_VALUE;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = opinionSide(MeasureSpec.getMode(widthMeasureSpec),
                MeasureSpec.getSize(widthMeasureSpec));
        mWidth = mHeight = size;
        setMeasuredDimension(mWidth, mHeight);
        mDrawBitmapRect.left = DEFAULT_NUM;
        mDrawBitmapRect.top = DEFAULT_NUM;
        mDrawBitmapRect.right = mWidth;
        mDrawBitmapRect.bottom = mHeight;
    }


    private int opinionSide(int mode, int size) {
        int result = DEFAULT_NUM;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int defaultSize = DEFAULT_SIDE;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, defaultSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInited == false) {
            setBitmapDegrees(DEFAULT_DEGREES);
            isInited = true;
        }
        canvas.drawBitmap(mDialBitmap, null, mDrawBitmapRect, null);
        canvas.drawBitmap(mPointerBitmap, mMatrix, null);

        if (drawControlLine) {
            RectF rectF = new RectF(4, 4, mWidth - 4, mHeight - 4);
            mPaint.setColor(0xffff0000);
            mPaint.setStrokeWidth(4);
            canvas.drawArc(rectF, DEFAULT_ARC_DEGREES, DEFAULT_ARC_DEGREES, false, mPaint);
            canvas.save();
            canvas.rotate(-DEFAULT_ARC_DEGREES, mWidth / 2, mHeight / 2);
            for (int i = 0; i <= DEFAULT_SCALE_VALUE; i++) {
                if (i % 10 == 0) {
                    canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight / 16, mPaint);
                } else {
                    canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight / 32, mPaint);
                }
                canvas.rotate(mRotateValues, mWidth / 2, mHeight / 2);
            }
            canvas.restore();
        }

        mPaint.setColor(0xffffffff);
        mPaint.setStrokeWidth(0);
        canvas.drawText(showDB + "db", mWidth / 2, mHeight / 4f * 3f, mPaint);
    }

    private void setBitmapDegrees(float degrees) {
        mMatrix = new Matrix();
        int w = mWidth;
        int h = mHeight;
        mMatrix.setRotate(-degrees, w / 2, h / 2);
        mPointerBitmap = Bitmap.createScaledBitmap(mPointerBitmap, mWidth, mHeight, true);
    }

    public void upDbValue(float value) {
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        showDB = decimalFormat.format(value);
        mMatrix.setRotate(-(DEFAULT_DEGREES - value * mRotateValues), getWidth() / 2, getHeight() / 2);
        invalidate();
    }
}
