package com.zsf.toolbox;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author EWorld  e-mail:852333743@qq.com
 * 2020/1/21
 * 设备信息
 */
public class SystemConfigActivity extends AppCompatActivity {
    private static final String TAG = "SystemConfigActivity";
    private TextView mMessage;
    private int mWidth;
    private int mHeight;
    private float mDensity;
    private int mDpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_config_msg);
        mMessage = findViewById(R.id.tv_message);

        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = findViewById(R.id.tv_title);
        title.setText("设备信息");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        mDensity = metrics.density;
        mDpi = metrics.densityDpi;

        //手机电量
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    /**
     * 获取手机内存大小
     *
     * @return
     */
    private String getTotalMemory() {
        String str1 = "/proc/meminfo";//系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();//读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.d(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);
    }

    /**
     * 获取当前可用内存大小
     *
     * @return
     */
    private String getAvailMemory() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return Formatter.formatFileSize(getBaseContext(), memoryInfo.availMem);
    }


    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");//获得当前电量
            int total = intent.getExtras().getInt("scale");//获得总电量
            int percent = current * 100 / total;
            StringBuffer sb = new StringBuffer();
            sb.append("主板：" + Build.BOARD);
            sb.append("\n系统启动程序版本号：" + Build.BOOTLOADER);
            sb.append("\n系统定制商：" + Build.BRAND);
            sb.append("\nCPU指令集：" + Build.CPU_ABI);
            sb.append("\n设置参数：" + Build.DEVICE);
            sb.append("\n显示屏参数：" + Build.DISPLAY);
            sb.append("\n无线电固件版本：" + Build.getRadioVersion());
            sb.append("\n硬件识别码：" + Build.FINGERPRINT);
            sb.append("\n硬件名称：" + Build.HARDWARE);
            sb.append("\n硬件制造商：" + Build.MANUFACTURER);
            sb.append("\n版本：" + Build.MODEL);
            sb.append("\n硬件序列号：" + Build.SERIAL);
            sb.append("\n手机制造商：" + Build.PRODUCT);
            sb.append("\nUSER:" + Build.USER);
            sb.append("\n系统版本：" + Build.VERSION.RELEASE);
            sb.append("\n手机当前电量：" + percent + "%");
            sb.append("\n手机屏幕高度：" + mHeight);
            sb.append("\n手机屏幕宽度：" + mWidth);
            sb.append("\n手机屏幕密度：" + mDensity);
            sb.append("\n手机屏幕密度DPI:" + mDpi);
            sb.append("\n手机内存大小：" + getTotalMemory());
            sb.append("\n当前可用内存：" + getAvailMemory());
            mMessage.setText(sb);

        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SystemConfigActivity.class);
        context.startActivity(intent);
    }
}
