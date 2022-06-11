package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zsf.toolbox.constant.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/6/10
 */
public class CatImgActivity extends AppCompatActivity {
    private static final String TAG = "CatImgActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTitleRightIv;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_img);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleRightIv = findViewById(R.id.iv_title_ok);
        mImageView = findViewById(R.id.iv_cat);
        mProgressBar = findViewById(R.id.iv_progress_bar);

        mTitleRightIv.setImageResource(R.drawable.icon_title_refresh_36);
        mTitleRightIv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("可爱小猫");
    }

    private void initEvent() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                initData();
            }
        });

    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) new URL(Constant.API_CAT_IMG).openConnection();
                    urlConnection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sBuilder = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        sBuilder.append(line).append("\n");
                    }
                    String result = sBuilder.toString();
                    Log.d(TAG, ">>>>>> result = " + result);
                    parse(new JSONArray(result));//将Json解析为文章列表
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        }.start();

    }

    private void parse(JSONArray jsonArray) {
        JSONObject itemObject = jsonArray.optJSONObject(0);
        String url = itemObject.optString("url");
        Log.d(TAG, ">>>>>> cat url = " + url);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(CatImgActivity.this).load(url).into(mImageView);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CatImgActivity.class);
        context.startActivity(intent);
    }
}
