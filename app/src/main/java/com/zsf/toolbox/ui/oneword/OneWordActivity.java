package com.zsf.toolbox.ui.oneword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.R;
import com.zsf.toolbox.api.OneWordApi;
import com.zsf.toolbox.bean.OneWord;
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
public class OneWordActivity extends AppCompatActivity {
    private static final String TAG = "OneWordActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mContentTv;
    private ImageView mRefreshIv;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_word);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mContentTv = findViewById(R.id.tv_content);
        mRefreshIv = findViewById(R.id.iv_title_ok);
        mRefreshIv.setImageResource(R.drawable.icon_title_refresh_36);
        mRefreshIv.setVisibility(View.VISIBLE);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("一言");
    }

    private void initEvent() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRefreshIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_ONE_WORD)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OneWordApi oneWordApi = retrofit.create(OneWordApi.class);
        Call<OneWord> call = oneWordApi.getOneWord();
        call.enqueue(new Callback<OneWord>() {
            @Override
            public void onResponse(Call<OneWord> call, Response<OneWord> response) {
                OneWord word = response.body();
                mContentTv.setText(word.getHitokoto() + "\n" + word.getFrom_who());
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OneWord> call, Throwable t) {
                Toast.makeText(OneWordActivity.this, "数据获取异常", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OneWordActivity.class);
        context.startActivity(intent);
    }
}
