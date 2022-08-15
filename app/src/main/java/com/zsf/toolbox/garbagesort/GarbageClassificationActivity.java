package com.zsf.toolbox.garbagesort;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zsf.toolbox.DogImgActivity;
import com.zsf.toolbox.R;
import com.zsf.toolbox.api.DogApi;
import com.zsf.toolbox.api.GarbageApi;
import com.zsf.toolbox.bean.DogBean;
import com.zsf.toolbox.bean.Garbage;
import com.zsf.toolbox.constant.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EWorld
 * 2022/7/1
 */
public class GarbageClassificationActivity extends AppCompatActivity {
    private static final String TAG = "GarbageClassificationAc";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mTickTv;
    private EditText mEditText;
    private ImageView mDeleteIv;
    private String mKeywords;
    private TextView mGarbageNameTv, mGarbageTypeTv, mResultTv;
    private boolean hasResult;//有结果

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_classification);
        initView();
        initEvent();

    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText("垃圾分类");
        mTickTv = findViewById(R.id.tv_start_search);
        mEditText = findViewById(R.id.et_input_garbage);
        mDeleteIv = findViewById(R.id.iv_delete);
        mDeleteIv.setVisibility(View.INVISIBLE);
        mGarbageNameTv = findViewById(R.id.tv_garbage_name);
        mGarbageTypeTv = findViewById(R.id.tv_garbage_type);
        mResultTv = findViewById(R.id.tv_result);
    }

    private void initEvent() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mTickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    mDeleteIv.setVisibility(View.INVISIBLE);
                } else if (count >= 0) {
                    mDeleteIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mKeywords = s.toString();
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        initData();
                        return true;
                }
                return false;
            }
        });

        mDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                mGarbageNameTv.setText("");
                mResultTv.setText("");
            }
        });
    }

    private void initData() {
        String keyword = mEditText.getText().toString();
        if (keyword.isEmpty()) {
            Toast.makeText(GarbageClassificationActivity.this, "请输入关键字！", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_GARBAGE_CLASSIFICATION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GarbageApi garbageApi = retrofit.create(GarbageApi.class);
        Call<Garbage> call = garbageApi.getGarbage();
        call.enqueue(new Callback<Garbage>() {
            @Override
            public void onResponse(Call<Garbage> call, Response<Garbage> response) {
                Garbage garbage = response.body();
                List<Garbage.DataDTO> garbageList = garbage.getData();
                for (int i = 0; i < garbageList.size(); i++) {
                    Garbage.DataDTO dataDTO = garbageList.get(i);
                    String word = dataDTO.getName();
                    Log.d(TAG, ">>>>>> word = " + word);
                    if (word.contains(mKeywords)) {
                        hasResult = true;
                        if (dataDTO.getType().equals("厨余垃圾") || dataDTO.getType().equals("湿垃圾") || dataDTO.getType().equals("厨余垃圾(湿垃圾)")) {
                            mGarbageNameTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_kitchen_waste, 0, 0, 0);
                        } else if (dataDTO.getType().equals("其他垃圾") || dataDTO.getType().equals("其他垃圾(干垃圾)")) {
                            mGarbageNameTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_other_garbage, 0, 0, 0);
                        } else if (dataDTO.getType().equals("可回收物") || dataDTO.getType().equals("可回收垃圾")) {
                            mGarbageNameTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_recyclable_waste, 0, 0, 0);
                        } else if (dataDTO.getType().equals("有害垃圾")) {
                            mGarbageNameTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_hazardous_waste, 0, 0, 0);
                        }
                        mGarbageNameTv.setText(dataDTO.getName());
                        mGarbageTypeTv.setText(dataDTO.getType());
                        mResultTv.setText(garbageList.get(i).getContain() + "\n"
                                + "\n"
                                + garbageList.get(i).getExplain() + "\n"
                                + "\n"
                                + "小提示！\n"
                                + garbageList.get(i).getTip());
                        break;
                    }
                }

                if (!hasResult) {
                    Toast.makeText(GarbageClassificationActivity.this, "抱歉，没有找到该类物品", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Garbage> call, Throwable t) {
                Log.d(TAG, ">>>>>>> t = " + t.toString());


            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GarbageClassificationActivity.class);
        context.startActivity(intent);
    }
}
