package com.zsf.toolbox.ui.gradienter;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsf.toolbox.R;


/**
 * Created by EWorld
 * 2021/7/22
 */
public class GradienterActivity extends AppCompatActivity implements SensorEventListener {
    private GradienterView mGradienterView;
    //定义水平仪能处理的最大倾斜角，超过该角度，气泡位于最边界
    private int MAX_ANGLE = 30;
    private SensorManager mSensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradienter);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGradienterView = findViewById(R.id.gradienter);

        TextView title = findViewById(R.id.tv_title);
        title.setText("水平仪");
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                //获取与Y轴的夹角
                float yAngle = values[1];
                //获取与Z轴的夹角
                float zAngle = values[2];
                //气泡位于中间时候(水平仪完全水平)，气泡的X，Y坐标
                int x = (mGradienterView.panel.getWidth() - mGradienterView.bubble.getWidth()) / 2;
                int y = (mGradienterView.panel.getHeight() - mGradienterView.bubble.getHeight()) / 2;
                //如果与Z轴的最大倾角还在最大角度内
                if (Math.abs(zAngle) <= MAX_ANGLE) {
                    int deltaX = (int) ((mGradienterView.panel.getWidth() - mGradienterView.bubble.getWidth()) / 2 * zAngle / MAX_ANGLE);
                    x += deltaX;
                } else if (zAngle > MAX_ANGLE) {
                    //与z轴的倾角大于最大，气泡应到最左边
                    x = 0;
                } else {
                    x = mGradienterView.panel.getWidth() - mGradienterView.bubble.getWidth();
                }

                //如果与Y轴的倾角还在最大角度内
                if (Math.abs(yAngle) <= MAX_ANGLE) {
                    int deltaY = (int) ((mGradienterView.panel.getHeight() - mGradienterView.bubble.getHeight()) / 2 * yAngle / MAX_ANGLE);
                    y += deltaY;
                } else if (yAngle > MAX_ANGLE) {
                    y = mGradienterView.panel.getHeight() - mGradienterView.bubble.getHeight();
                } else {
                    y = 0;
                }
                //如果计算出来的X，Y坐标在水平仪的表盘内，更新水平仪气泡坐标
                if (isContain(x, y)) {
                    mGradienterView.bubbleX = x;
                    mGradienterView.bubbleY = y;
                }
                mGradienterView.postInvalidate();
                break;
        }
    }

    private boolean isContain(int x, int y) {
        //计算气泡的圆心坐标x,y
        int bubbleCenterX = x + mGradienterView.bubble.getWidth() / 2;
        int bubbleCenterY = y + mGradienterView.bubble.getHeight() / 2;
        //计算水平仪仪表盘的圆心坐标
        int panelX = mGradienterView.panel.getWidth() / 2;
        int panelY = mGradienterView.panel.getHeight() / 2;
        //计算气泡的圆心与水平仪表盘的圆心之间的距离
        double distance = Math.sqrt((bubbleCenterX - panelX) * (bubbleCenterX - panelX) + (bubbleCenterY - panelY) * (bubbleCenterY - panelY));
        if (distance < (mGradienterView.panel.getWidth() - mGradienterView.bubble.getWidth()) / 2) {
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GradienterActivity.class);
        context.startActivity(intent);
    }
}
