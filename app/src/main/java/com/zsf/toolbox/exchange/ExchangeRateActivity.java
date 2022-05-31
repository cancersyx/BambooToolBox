package com.zsf.toolbox.exchange;

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

import com.zsf.toolbox.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);

        mBack = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText("汇率换算");
        mChooseCurrency0 = findViewById(R.id.tv_currency_name);
        mChooseCurrency1 = findViewById(R.id.tv_currency_name_);
        mInputCurrency0 = findViewById(R.id.et_input_money);
        mInputCurrency1 = findViewById(R.id.tv_input_money_);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("click_item");
            Log.d(TAG, ">>>>>> name = " + name);
            mChooseCurrency0.setText(name);
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("click_item");
            mChooseCurrency1.setText(name);
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ExchangeRateActivity.class);
        context.startActivity(intent);
    }
}
