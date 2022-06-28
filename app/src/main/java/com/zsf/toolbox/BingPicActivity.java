package com.zsf.toolbox;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.api.BingPicApi;
import com.zsf.toolbox.constant.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by EWorld
 * 2022/6/21
 */
public class BingPicActivity extends AppCompatActivity {
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTitleRightIv;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_pic);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleRightIv = findViewById(R.id.iv_title_ok);
        mImageView = findViewById(R.id.iv_bing);
        mProgressBar = findViewById(R.id.iv_progress_bar);

        mTitleRightIv.setImageResource(R.drawable.icon_title_refresh_36);
        mTitleRightIv.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("每日Bing");
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BING_DAILY_2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BingPicApi bingPicApi = retrofit.create(BingPicApi.class);
        Call call = bingPicApi.getBingPic();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = (ResponseBody) response.body();
                    if (responseBody == null) return;
                    mBitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                    mImageView.setImageBitmap(mBitmap);
                    mProgressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
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
        Intent intent = new Intent(context, BingPicActivity.class);
        context.startActivity(intent);
    }
}
