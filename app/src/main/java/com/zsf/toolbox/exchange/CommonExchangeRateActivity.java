package com.zsf.toolbox.exchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.R;
import com.zsf.toolbox.TaoBaoActivity;
import com.zsf.toolbox.api.CommonExchangeRateAPI;
import com.zsf.toolbox.api.CurrencyAPI;
import com.zsf.toolbox.bean.CommonExchangeRateBean;
import com.zsf.toolbox.bean.Currency;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/6/14
 * 常用汇率
 */
public class CommonExchangeRateActivity extends AppCompatActivity {
    private static final String TAG = "CommonExchangeRateActiv";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ListView mListView;
    private TextView mUpdateTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_exchange_rate);
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mListView = findViewById(R.id.common_rate_list_view);
        mUpdateTv = findViewById(R.id.tv_update_time);

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_JVHE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommonExchangeRateAPI commonRateAPI = retrofit.create(CommonExchangeRateAPI.class);
        Call<CommonExchangeRateBean> call = commonRateAPI.getCommonRateList();
        call.enqueue(new Callback<CommonExchangeRateBean>() {
            @Override
            public void onResponse(Call<CommonExchangeRateBean> call, Response<CommonExchangeRateBean> response) {
                CommonExchangeRateBean commonExchangeRateBean = response.body();
                if (commonExchangeRateBean != null) {
                    if (commonExchangeRateBean.getError_code() == 10012) {
                        Toast.makeText(CommonExchangeRateActivity.this, "超过每日请求次数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.d(TAG, ">>>>>> result = " + commonExchangeRateBean);
                CommonRateAdapter adapter = new CommonRateAdapter(commonExchangeRateBean.getResult().getList());
                mListView.setAdapter(adapter);
                mUpdateTv.setText("更新时间：" + commonExchangeRateBean.getResult().getUpdate());
            }

            @Override
            public void onFailure(Call<CommonExchangeRateBean> call, Throwable t) {

            }
        });


    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CommonExchangeRateActivity.class);
        context.startActivity(intent);
    }
}
