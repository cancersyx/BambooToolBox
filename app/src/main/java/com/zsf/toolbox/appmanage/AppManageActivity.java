package com.zsf.toolbox.appmanage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zsf.toolbox.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EWorld
 * 2022/6/9
 */
public class AppManageActivity extends AppCompatActivity {
    private static final String TAG = "AppManageActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ListView mListView;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage);

        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText("应用管理");
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mListView = findViewById(R.id.app_list_view);
        new Thread() {
            @Override
            public void run() {
                List<AppInfo> appInfos = getAppInfos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        mListView.setAdapter(new AppAdapter(AppManageActivity.this, appInfos));

                    }
                });

            }
        }.start();

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<AppInfo> getAppInfos() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            AppInfo appInfo = new AppInfo();
            appInfo.icon = packageInfo.applicationInfo.loadIcon(packageManager);
            appInfo.label = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            appInfo.package_name = packageInfo.packageName;
            appInfo.version = packageInfo.versionName;
            String dir = packageInfo.applicationInfo.publicSourceDir;
            int size = Integer.valueOf((int) new File(dir).length());
            appInfo.size = parseApkSize(size);
            appInfos.add(appInfo);
        }

        return appInfos;
    }

    private BigDecimal parseApkSize(int size) {
        BigDecimal bd = new BigDecimal((double) size / (1024 * 1024));
        BigDecimal setScale = bd.setScale(2, BigDecimal.ROUND_DOWN);
        return setScale;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AppManageActivity.class);
        context.startActivity(intent);
    }
}
