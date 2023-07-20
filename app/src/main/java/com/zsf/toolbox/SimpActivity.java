package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsf.toolbox.api.SimpApi;
import com.zsf.toolbox.bean.SimpDog;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by EWorld
 * 2022/6/9
 * 添狗日记
 */
public class SimpActivity extends AppCompatActivity {
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mContentTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simp);

        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mContentTv = findViewById(R.id.tv_content);

        mTitleTv.setText("添狗日记");
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_SIMP)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SimpApi simpApi = retrofit.create(SimpApi.class);
        Call<SimpDog> call = simpApi.getSimpDiary();
        call.enqueue(new Callback<SimpDog>() {
            @Override
            public void onResponse(Call<SimpDog> call, Response<SimpDog> response) {
                SimpDog simpDog = response.body();
                mContentTv.setText(simpDog.getData());
            }

            @Override
            public void onFailure(Call<SimpDog> call, Throwable t) {

            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SimpActivity.class);
        context.startActivity(intent);
    }
}
