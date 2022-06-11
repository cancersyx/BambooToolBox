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
import com.zsf.toolbox.api.IpApi;
import com.zsf.toolbox.bean.DogBean;
import com.zsf.toolbox.bean.IpBean;
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
public class IpAddressActivity extends AppCompatActivity {
    private static final String TAG = "IpAddressActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mIpContent;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mIpContent = findViewById(R.id.tv_ip_address);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTitleTv.setText("IP地址");

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_IP_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpApi ipApi = retrofit.create(IpApi.class);
        Call<IpBean> call = ipApi.getIp();
        call.enqueue(new Callback<IpBean>() {
            @Override
            public void onResponse(Call<IpBean> call, Response<IpBean> response) {
                IpBean ipBean = response.body();
                mIpContent.setText("当前IP地址：" + ipBean.getIp());
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<IpBean> call, Throwable t) {
                Toast.makeText(IpAddressActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, IpAddressActivity.class);
        context.startActivity(intent);
    }
}
