package com.zsf.toolbox.ui.decibel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.R;


/**
 * @author EWorld  e-mail:852333743@qq.com
 * 2020/1/13
 */
public class DecibelActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_AUDIO_RECORD = 101;
    private DialPlateView mPlateView;
    private AudioRecordManager mRecordManager;
    private boolean isStart;
    private ImageView mBackIv;
    private TextView mTitleTv;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                double values = (double) msg.obj;
                float db = (float) values;
                mPlateView.upDbValue(db);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decibel);
        mPlateView = findViewById(R.id.dial_plate_view);
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText("分贝测试");

        mRecordManager = new AudioRecordManager(mHandler, 100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_AUDIO_RECORD);
        } else {
            startTest();
        }

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void startTest() {
        mRecordManager.getNoiseLevel();
        isStart = true;
        Toast.makeText(this, "开启分贝测试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_AUDIO_RECORD && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTest();
        } else {
            Toast.makeText(this, "未授予录音权限，无法使用该功能！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isStart) {
            AudioRecordManager.isGetVoiceRun = false;
            isStart = false;
            Toast.makeText(this, "关闭分贝测试", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DecibelActivity.class);
        context.startActivity(intent);
    }
}
