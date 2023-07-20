package com.zsf.toolbox.ui.netspeed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsf.toolbox.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by EWorld
 * 2022/5/30
 */
public class NetSpeedActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NetSpeedActivity";
    private ImageView mBackIv;
    private TextView mTitle;
    private TextView mStartNet;
    private TextView mNetType;
    private TextView mCurrentSpeedTv, mAverageSpeedTv;

    private NetDwInfo mNetDwInfo;
    private boolean flag;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x100:
                    mCurrentSpeedTv.setText("当前速度：" + msg.arg1 + "KB/S");
                    mAverageSpeedTv.setText("平均速度：" + msg.arg2 + "KB/S");
                    break;
                case 0x101:
                    mCurrentSpeedTv.setText("当前速度：0KB/S");
                    mStartNet.setText("开始测试");
                    mStartNet.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_speed);

        mBackIv = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);
        mStartNet = findViewById(R.id.tv_start_test_net);
        mNetType = findViewById(R.id.tv_net_type);
        mCurrentSpeedTv = findViewById(R.id.tv_current_speed);
        mAverageSpeedTv = findViewById(R.id.tv_ave_speed);

        mTitle.setText("网络测速");

        mNetDwInfo = new NetDwInfo();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d(TAG, ">>>>>>>networkInfo.typeName =  " + networkInfo.getTypeName());
        mNetType.setText(networkInfo.getTypeName());

        mBackIv.setOnClickListener(this);
        mStartNet.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start_test_net:
                mStartNet.setText("测试中...");
                mStartNet.setEnabled(false);
                mNetDwInfo.totalByte = 1024;
                mNetDwInfo.dwordByte = 0;
                mNetDwInfo.speed = 0;
                new DownloadThread().start();
                new GetInfoThread().start();
                break;
        }


    }

    private class DownloadThread extends Thread {
        @Override
        public void run() {
            super.run();
            //String urlStr = "https://n.sinaimg.cn/sinakd20118/120/w1080h1440/20200803/4166-ixeeirz8494239.jpg";
            String urlStr = "https://github.com/cancersyx/AppPrivacy/raw/main/apk/BambooWeather_20220517_V2.6.5.apk";
            long startTime, currentTime;
            URL url;
            URLConnection connection = null;
            InputStream is = null;
            try {
                url = new URL(urlStr);
                connection = url.openConnection();

                mNetDwInfo.totalByte = connection.getContentLength();
                Log.d(TAG, ">>>>>> totalByte = " + mNetDwInfo.totalByte + " ,flag = " + flag);
                is = connection.getInputStream();
                startTime = System.currentTimeMillis();
                while (is.read() != -1 && flag) {
                    mNetDwInfo.dwordByte++;
                    currentTime = System.currentTimeMillis();
                    if (currentTime - startTime == 0) {
                        mNetDwInfo.speed = 1000;
                    } else {
                        mNetDwInfo.speed = mNetDwInfo.dwordByte / (currentTime - startTime) * 1000;
                        Log.d(TAG, ">>>>>> speed = " + mNetDwInfo.speed);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private class GetInfoThread extends Thread {
        @Override
        public void run() {
            double sum;
            double counter;
            int currentSpeed, averageSpeed;
            try {
                sum = 0;
                counter = 0;
                Log.d(TAG, ">>>>>> 99 dwordByte = " + mNetDwInfo.dwordByte + " ,totalByte = " + mNetDwInfo.totalByte);
                while (mNetDwInfo.dwordByte < mNetDwInfo.totalByte && flag) {
                    Thread.sleep(1000);
                    sum += mNetDwInfo.speed;
                    counter++;
                    currentSpeed = (int) mNetDwInfo.speed;
                    averageSpeed = (int) (sum / counter);
                    Log.d(TAG, ">>>>>> current speed = " + mNetDwInfo.speed / 1024 + "KB/S");
                    Message msg = new Message();
                    msg.arg1 = ((int) mNetDwInfo.speed / 1024);
                    msg.arg2 = ((int) averageSpeed / 1024);
                    msg.what = 0x100;
                    mHandler.sendMessage(msg);
                }
                Log.d(TAG, ">>>>>>> dwordByte = " + mNetDwInfo.dwordByte + " ,total.byte = " + mNetDwInfo.totalByte);
                if (mNetDwInfo.dwordByte >= mNetDwInfo.totalByte && flag) {
                    mHandler.sendEmptyMessage(0x101);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flag = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = true;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, NetSpeedActivity.class);
        context.startActivity(intent);
    }
}
