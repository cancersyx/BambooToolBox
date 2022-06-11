package com.zsf.toolbox.oneword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsf.toolbox.R;
import com.zsf.toolbox.api.OneWordApi;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_word);
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mContentTv = findViewById(R.id.tv_content);

        mTitleTv.setText("一言");
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_ONE_WORD)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        OneWordApi oneWordApi = retrofit.create(OneWordApi.class);
        Call<String> call = oneWordApi.getOneWord();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String content = response.body();
                mContentTv.setText(content);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OneWordActivity.class);
        context.startActivity(intent);
    }
}
