package com.zsf.toolbox.ruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by EWorld
 * 2021/11/27
 * 自定义尺子
 */
public class RulerView extends View {
    private float cm;//厘米
    private float mm;//毫米
    private float rulerWidth;
    private float rulerHeight;
    private PointF middlePoint = new PointF(0, 0);
    private float rotateAngle = 90;//旋转角度
    private PointF touchPoint = new PointF(0, 0);

    private float mScreenWidth;
    private final Paint mPaint;
    private final Paint mAuxiliaryPaint;//辅助线


    public RulerView(Context context, DisplayMetrics metrics) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(20);

        mAuxiliaryPaint = new Paint();
        mAuxiliaryPaint.setColor(Color.RED);
        mAuxiliaryPaint.setStyle(Paint.Style.STROKE);
        mAuxiliaryPaint.setStrokeWidth(2);

        cm = (float) (metrics.xdpi / 2.54);
        mm = cm / 10;
        rulerWidth = 50 * cm;
        rulerHeight = 2 * cm;
        mScreenWidth = metrics.widthPixels;
        middlePoint.set(metrics.widthPixels, (float) (rulerWidth * 0.5));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(rotateAngle, middlePoint.x, middlePoint.y);
        canvas.drawRect(middlePoint.x - rulerWidth / 2, middlePoint.y,
                middlePoint.x + rulerWidth / 2, middlePoint.y + rulerHeight, mPaint);
        for (int i = 0; i < rulerWidth / mm; i++) {
            float left = middlePoint.x - rulerWidth / 2;
            if (i % 10 == 0 && i != 0) {
                canvas.drawLine(left + i * mm, middlePoint.y, left + i * mm, middlePoint.y + 50, mPaint);
                canvas.drawText(Integer.toString(i / 10), left + i * mm, middlePoint.y + 55, mPaint);
            } else if (i == 0) {
                canvas.drawLine(left + i * mm, middlePoint.y, left + i * mm, middlePoint.y + 50, mPaint);
                canvas.drawText(Integer.toString(i / 5) + "cm", left + i * mm, middlePoint.y + 55, mPaint);
            } else {
                canvas.drawLine(left + i * mm, middlePoint.y, left + i * mm, middlePoint.y + 30, mPaint);
            }
        }
        canvas.restore();
        //绘制辅助红线
        canvas.drawLine(0, touchPoint.y, mScreenWidth, touchPoint.y, mAuxiliaryPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //event.getAction() & MotionEvent.ACTION_MASK 可以处理处理多点触摸的
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                touchPoint.set(event.getX(), event.getY());
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }
}
