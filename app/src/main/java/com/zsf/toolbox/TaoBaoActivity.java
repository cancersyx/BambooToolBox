package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zsf.toolbox.api.TaoBaoApi;
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

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * Created by EWorld
 * 2022/6/10
 */
public class TaoBaoActivity extends AppCompatActivity {
    private static final String TAG = "TaoBaoActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTaoBaoPic;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);

        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTaoBaoPic = findViewById(R.id.iv_pic);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("淘宝买家秀");

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTaoBaoPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mBitmap != null) {
                    String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getPath();
                    saveImage(mBitmap, path);
                }
                return true;
            }
        });
        initData();
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_TAO_BAO_IMG)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TaoBaoApi taoBaoApi = retrofit.create(TaoBaoApi.class);
        Call call = taoBaoApi.getTaoBaoPic();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = (ResponseBody) response.body();
                    if (responseBody == null) return;
                    mBitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                    mTaoBaoPic.setImageBitmap(mBitmap);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TaoBaoActivity.class);
        context.startActivity(intent);
    }
}
