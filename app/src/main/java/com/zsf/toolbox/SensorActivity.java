package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by EWorld
 * 2022/6/30
 *
 * 加速传感器    　　 Sensor.TYPE_ACCELEROMETER
 * 陀螺仪传感器  　   Sensor.TYPE_GYROSCOPE
 * 环境光仪传感器     Sensor.TYPE_LIGHT
 * 电磁场传感器    　 Sensor.TYPE_MAGNETIC_FIELD
 * 方向传感器    　　 Sensor.TYPE_ORIENTATION:
 * 压力传感器    　　 Sensor.TYPE_PRESSURE:
 * 距离传感器   　　  Sensor.TYPE_PROXIMITY:
 * 温度传感器   　　  Sensor.TYPE_TEMPERATURE:
 */
public class SensorActivity extends AppCompatActivity {
    private static final String TAG = "SensorActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTitleRightIv;
    private TextView mSensorCountTv, mSensorEnumTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        initView();
        initEvent();
        initData();


    }

    private void initData() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        mSensorCountTv.setText("共扫描到" + allSensors.size() + "种传感器");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < allSensors.size(); i++) {
            if (allSensors.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {
                builder.append("加速传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_TEMPERATURE) {
                builder.append("温度传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_GYROSCOPE) {
                builder.append("陀螺仪传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_LIGHT) {
                builder.append("光线传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                builder.append("磁场传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_ORIENTATION) {
                builder.append("方向传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_PRESSURE) {
                builder.append("压力传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_PROXIMITY) {
                builder.append("距离传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_GRAVITY) {
                builder.append("重力传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                builder.append("线性加速度 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_ROTATION_VECTOR) {
                builder.append("旋转矢量传感器 " + "\n");
            } else if (allSensors.get(i).getType() == Sensor.TYPE_HEART_BEAT) {
                builder.append("心跳传感器 " + "\n");
            }else if (allSensors.get(i).getType() == Sensor.TYPE_HEART_RATE){
                builder.append("心率传感器 " + "\n");
            }else if (allSensors.get(i).getType()==Sensor.TYPE_STEP_COUNTER){
                builder.append("计步传感器 " + "\n");
            }else if (allSensors.get(i).getType()==Sensor.TYPE_STEP_DETECTOR){
                builder.append("步测传感器 " + "\n");
            }
            builder.append("Name: " + allSensors.get(i).getName() + "\n");
            builder.append("Vendor: " + allSensors.get(i).getVendor() + "\n");
            builder.append("MinDelay: " + allSensors.get(i).getMinDelay() + "\n");
            builder.append("Power: " + allSensors.get(i).getPower() + "\n");
            builder.append("Resolution: " + allSensors.get(i).getResolution() + "\n");
            builder.append("MaximumRange: " + allSensors.get(i).getMaximumRange() + "\n");
            builder.append("\n");
            builder.append("\n");
            Log.d(TAG, ">>>>>>> sensor_" + i + " = " + allSensors.get(i).toString());
        }
        mSensorEnumTv.setText(builder.toString());
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        String name = sensor.getName();
        String vendor = sensor.getVendor();
        Log.d(TAG, ">>>>>> name = " + name + " ,vendor = " + vendor);
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleRightIv = findViewById(R.id.iv_title_ok);
        mTitleTv.setText("传感器");
        mSensorCountTv = findViewById(R.id.tv_sensor_total);
        mSensorEnumTv = findViewById(R.id.tv_sensor_enum);
        mSensorEnumTv.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    private void initEvent() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SensorActivity.class);
        context.startActivity(intent);
    }
}
