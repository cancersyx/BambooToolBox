package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zsf.toolbox.api.DogApi;
import com.zsf.toolbox.bean.DogBean;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/6/10
 */
public class DogImgActivity extends AppCompatActivity {
    private static final String TAG = "DogImgActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTitleRightIv;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_img);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleRightIv = findViewById(R.id.iv_title_ok);
        mImageView = findViewById(R.id.iv_dog);
        mProgressBar = findViewById(R.id.iv_progress_bar);

        mTitleRightIv.setImageResource(R.drawable.icon_title_refresh_36);
        mTitleRightIv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("可爱小狗");
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_RANDOM_DOG_IMG)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DogApi dogApi = retrofit.create(DogApi.class);
        Call<DogBean> call = dogApi.getDog();
        call.enqueue(new Callback<DogBean>() {
            @Override
            public void onResponse(Call<DogBean> call, Response<DogBean> response) {
                DogBean dogBean = response.body();
                String imgUrl = dogBean.getMessage();
                Log.d(TAG, ">>>>>> imgUrl = " + imgUrl);
                if (!isDestroyed() || !isFinishing()) {
                    Glide.with(DogImgActivity.this).load(imgUrl).into(mImageView);
                }
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<DogBean> call, Throwable t) {
                Log.d(TAG, ">>>>>>> t = " + t.toString());
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(DogImgActivity.this, "加载图片失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DogImgActivity.class);
        context.startActivity(intent);
    }
}
