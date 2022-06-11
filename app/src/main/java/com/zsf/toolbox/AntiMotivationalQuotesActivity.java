package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsf.toolbox.api.AntiMotivationalQuotesApi;
import com.zsf.toolbox.api.SimpApi;
import com.zsf.toolbox.bean.AntiMotivationalQuotesBean;
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
 */

public class AntiMotivationalQuotesActivity extends AppCompatActivity {
    private static final String TAG = "AntiMotivationalQuotesA";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mContentTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_motivational_quotes);

        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mContentTv = findViewById(R.id.tv_content);

        mTitleTv.setText("毒鸡汤");
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_ANTI_MOTIVATIONAL_QUOTES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AntiMotivationalQuotesApi antiMotivationalQuotesApi = retrofit.create(AntiMotivationalQuotesApi.class);
        Call<AntiMotivationalQuotesBean> call = antiMotivationalQuotesApi.getAntiMotivationalQuotes();
        call.enqueue(new Callback<AntiMotivationalQuotesBean>() {
            @Override
            public void onResponse(Call<AntiMotivationalQuotesBean> call, Response<AntiMotivationalQuotesBean> response) {
                AntiMotivationalQuotesBean bean = response.body();
                mContentTv.setText(bean.getData());
            }

            @Override
            public void onFailure(Call<AntiMotivationalQuotesBean> call, Throwable t) {
                Log.d(TAG, ">>>>>> t = " + t.toString());
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AntiMotivationalQuotesActivity.class);
        context.startActivity(intent);
    }
}
