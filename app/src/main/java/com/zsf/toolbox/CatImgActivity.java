package com.zsf.toolbox;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Environment.DIRECTORY_PICTURES;

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

    private Bitmap mBitmap;

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
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!hasPermission()) {
                    requestPermission();
                    return true;
                }
                if (mBitmap != null) {
                    String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getPath();
                    saveImage(mBitmap, path);
                }
                return true;
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

        try {
            mBitmap = Glide.with(CatImgActivity.this).asBitmap().load(url).into(mImageView.getWidth(), mImageView.getHeight()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    private void saveImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "PNG_" + timeStamp + "_";

            fileOutputStream = new FileOutputStream(path + "/" + imageFileName + ".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            Toast.makeText(this, "图片保存到" + path, Toast.LENGTH_SHORT).show();

            File picFile = new File(path + "/" + imageFileName + ".png");
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, picFile.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mBitmap != null) {
                    String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getPath();
                    saveImage(mBitmap, path);
                }
            } else {
                Toast.makeText(this, "禁止权限会影响存储功能！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CatImgActivity.class);
        context.startActivity(intent);
    }
}
