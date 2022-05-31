package com.zsf.toolbox.exchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zsf.toolbox.R;
import com.zsf.toolbox.api.CurrencyAPI;
import com.zsf.toolbox.bean.Currency;
import com.zsf.toolbox.constant.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/5/28
 */
public class CurrencyListActivity extends AppCompatActivity {
    private static final String TAG = "CurrencyListActivity";
    private ImageView mBack;
    private TextView mTitle;
    private ListView mListView;
    private List<Currency.ResultDTO.ListDTO> mCurrencyDatas;
    private int mFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);
        mBack = findViewById(R.id.iv_back);
        mTitle = findViewById(R.id.tv_title);
        mTitle.setText("选择货币");
        mListView = findViewById(R.id.list_view);

        mFlag = getIntent().getIntExtra("flag_is_which_btn", 0);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_JVHE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CurrencyAPI currencyAPI = retrofit.create(CurrencyAPI.class);
        Call<Currency> call = currencyAPI.getCurrencyList();
        call.enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(Call<Currency> call, Response<Currency> response) {
                Log.d(TAG, ">>>>>>> response = " + response);
                Currency currency = response.body();
                if (currency.getError_code() == 10012) {
                    Log.d(TAG, ">>>>>> 超过每日可允许请求次数");
                    return;
                }
                if (currency.getResult() == null) return;
                mCurrencyDatas = currency.getResult().getList();
                if (mCurrencyDatas == null) return;
                mListView.setAdapter(new ArrayAdapter<Currency.ResultDTO.ListDTO>(CurrencyListActivity.this, 0, mCurrencyDatas) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        if (convertView == null) {
                            convertView = LayoutInflater.from(CurrencyListActivity.this).inflate(R.layout.item_currency, parent, false);
                        }

                        TextView textView = convertView.findViewById(R.id.tv_name);
                        Currency.ResultDTO.ListDTO dto = getItem(position);
                        textView.setText(dto.getName() + "-" + dto.getCode());
                        return convertView;
                    }
                });

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("click_item", mCurrencyDatas.get(position).getName() + "-" + mCurrencyDatas.get(position).getCode());
                       /* if (mFlag == 0) {
                            //setResult(101, intent);
                            setResult(RESULT_OK,intent);
                        } else {
                            setResult(102, intent);
                        }*/
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(Call<Currency> call, Throwable t) {

            }
        });
    }

    public static void startActivityForResult(Activity context, int flag) {
        Intent intent = new Intent(context, CurrencyListActivity.class);
        intent.putExtra("flag_is_which_btn", flag);
        context.startActivityForResult(intent, flag == 0 ? 101 : 102);
    }
}
