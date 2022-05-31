package com.zsf.toolbox.gradienter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.zsf.toolbox.R;


/**
 * Created by EWorld
 * 2021/7/24
 */
public class GradienterView extends View {
    public Bitmap panel;
    //气泡图
    public Bitmap bubble;
    //气泡的X坐标Y坐标
    public int bubbleX, bubbleY;


    public GradienterView(Context context) {
        this(context, null);
    }

    public GradienterView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradienterView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取窗口管理器
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕的宽度和高度
        Display display = wm.getDefaultDisplay();
        // TODO: 2021/7/24  了解下这个类
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        //创建位图
        panel = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(panel);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        //创建一个线性渐变
        Shader shader = new LinearGradient(0, screenWidth, screenWidth * 0.8f,
                screenWidth * 0.2f, Color.YELLOW, Color.WHITE, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        //绘制圆形
        canvas.drawCircle(screenWidth / 2, screenWidth / 2, screenWidth / 2, paint);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(5);
        paint2.setColor(Color.BLACK);
        canvas.drawCircle(screenWidth / 2, screenWidth / 2, screenWidth / 2, paint2);
        //绘制水平横线
        canvas.drawLine(0, screenWidth / 2, screenWidth, screenWidth / 2, paint2);
        //绘制垂直横线
        canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenWidth, paint2);
        paint2.setStrokeWidth(10);
        paint2.setColor(Color.RED);
        //绘制中心的红色十字
        canvas.drawLine(screenWidth / 2 - 30, screenWidth / 2, screenWidth / 2 + 30, screenWidth / 2, paint2);
        canvas.drawLine(screenWidth / 2, screenWidth / 2 - 30, screenWidth / 2, screenWidth / 2 + 30, paint2);
        //加载气泡图片
        bubble = BitmapFactory.decodeResource(getResources(), R.drawable.icon_gradienter_bubble);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制水平仪图片
        canvas.drawBitmap(panel, 0, 0, null);
        //根据气泡坐标绘制气泡
        canvas.drawBitmap(bubble, bubbleX, bubbleY, null);
    }
}
