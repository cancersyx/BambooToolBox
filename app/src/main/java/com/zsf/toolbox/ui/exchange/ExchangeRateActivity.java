package com.zsf.toolbox.ui.exchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.R;
import com.zsf.toolbox.api.CurrentRateApi;
import com.zsf.toolbox.bean.CurrentRate;
import com.zsf.toolbox.constant.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/5/27
 * 汇率换算
 */
public class ExchangeRateActivity extends AppCompatActivity {
    private static final String TAG = "ExchangeRateActivity";
    private ImageView mBack;
    private TextView mTitle;
    private TextView mChooseCurrency0, mChooseCurrency1;
    private EditText mInputCurrency0;
    private TextView mInputCurrency1;
    private TextView mCommonExchangeRateTv;
    private TextView mCalculateRateTv;
    private float mRate;
    private String mFrom, mTo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        initView();
        initEvent();
        mFrom = "CNY";
        mTo = "USD";
        initData("CNY", "USD");
    }

    private void initEvent() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCommonExchangeRateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonExchangeRateActivity.startActivity(ExchangeRateActivity.this);
            }
        });

        mChooseCurrency0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyListActivity.startActivityForResult(ExchangeRateActivity.this, 0);
            }
        });

        mChooseCurrency1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyListActivity.startActivityForResult(ExchangeRateActivity.this, 1);
            }
        });
        mCalculateRateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(mFrom, mTo);
            }
        });
    }

    private void initView() {
        mBack = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText("汇率换算");
        mChooseCurrency0 = findViewById(R.id.tv_currency_name);
        mChooseCurrency1 = findViewById(R.id.tv_currency_name_);
        mInputCurrency0 = findViewById(R.id.et_input_money);
        mInputCurrency1 = findViewById(R.id.tv_input_money_);
        mCommonExchangeRateTv = findViewById(R.id.tv_common_exchange_rate);
        mCalculateRateTv = findViewById(R.id.tv_calculate_rate);
    }

    private void initData(String from, String to) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_JVHE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CurrentRateApi currentRateApi = retrofit.create(CurrentRateApi.class);
        Call<CurrentRate> call = currentRateApi.getCurrentRate(from, to);
        call.enqueue(new Callback<CurrentRate>() {
            @Override
            public void onResponse(Call<CurrentRate> call, Response<CurrentRate> response) {
                try {
                    CurrentRate currentRate = response.body();
                    if (currentRate.getError_code() == 10012) {
                        Log.d(TAG, ">>>>>> 超过每日可允许请求次数");
                        Toast.makeText(ExchangeRateActivity.this, "超过每日可请求次数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String rateResult = currentRate.getResult().get(0).getResult();
                    mRate = Float.parseFloat(rateResult);
                    Log.d(TAG, ">>>>>> rate = " + mRate);
                    float fromCurrency = Integer.parseInt(mInputCurrency0.getText().toString());
                    float toCurrency = (fromCurrency * mRate);
                    mInputCurrency1.setText(toCurrency + "");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<CurrentRate> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("click_item");
            Log.d(TAG, ">>>>>> name = " + name);
            mChooseCurrency0.setText(name);
            mFrom = name.split("-")[1];
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("click_item");
            mChooseCurrency1.setText(name);
            mTo = name.split("-")[1];
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ExchangeRateActivity.class);
        context.startActivity(intent);
    }
}
