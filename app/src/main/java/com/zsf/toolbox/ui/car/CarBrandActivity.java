package com.zsf.toolbox.ui.car;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.R;
import com.zsf.toolbox.api.CarBrandApi;
import com.zsf.toolbox.bean.CarBrandBean;
import com.zsf.toolbox.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/11/21
 */
public class CarBrandActivity extends AppCompatActivity {
    private static final String TAG = "CarBrandActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTitleRightIv;
    private EditText mInputEt;
    private ListView mListView;

    private List<CarBrandBean.ResultDTO> mCarBrandList;
    private CarBrandAdapter mCarBrandAdapter;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleRightIv = findViewById(R.id.iv_title_ok);
        mInputEt = findViewById(R.id.et_input);
        mListView = findViewById(R.id.list_view);

        mTitleRightIv.setImageResource(R.drawable.icon_title_refresh_36);
        mTitleRightIv.setVisibility(View.INVISIBLE);

        mTitleTv.setText("汽车品牌");

        mCarBrandList = new ArrayList<>();
        mCarBrandAdapter = new CarBrandAdapter(this, mCarBrandList);
        mListView.setAdapter(mCarBrandAdapter);
    }

    private void initEvent() {
        mBackIv.setOnClickListener(v -> finish());

        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mInputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = mInputEt.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(CarBrandActivity.this, "请输入关键字！", Toast.LENGTH_SHORT).show();
                    } else {
                        searchBrand(content);
                        closeInputMethod();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initData() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_JUHE_CAR_BRAND)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchBrand("A");
    }

    private void searchBrand(String firstLetter) {
        Log.d(TAG, ">>>>>>>> firstLetter = " + firstLetter);
        CarBrandApi carBrandApi = mRetrofit.create(CarBrandApi.class);
        Call<CarBrandBean> call = carBrandApi.getCarBrand(firstLetter);
        call.enqueue(new Callback<CarBrandBean>() {
            @Override
            public void onResponse(Call<CarBrandBean> call, Response<CarBrandBean> response) {
                CarBrandBean carBrandBean = response.body();
                mCarBrandAdapter.refreshAll(carBrandBean.getResult());
            }

            @Override
            public void onFailure(Call<CarBrandBean> call, Throwable t) {

            }
        });
    }

    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(mInputEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void openInputMethod() {

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CarBrandActivity.class);
        context.startActivity(intent);
    }
}
