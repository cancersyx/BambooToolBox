package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zsf.toolbox.garbagesort.GarbageClassificationActivity;

import java.util.Random;

/**
 * Created by EWorld
 * 2022/8/12
 */
public class FeedActivity extends AppCompatActivity {
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mTitleRightIv;
    private RadioButton mWorkingLunchBtn, mDineTogetherBtn;
    private TextView mFoodContentTv;
    private TextView mStartTv;

    private String[] mWorkingLunchArray = new String[]{
            "沙县小吃", "黄焖鸡米饭", "兰州拉面", "米线", "凉皮", "麻辣烫", "肯德基", "麦当劳", "牛肉面",
            "烩面", "烤肉饭", "饺子", "华莱士", "花甲粉", "盖浇饭", "汉堡王", "麻辣香锅", "生煎", "炒饭炒面",
            "螺蛳粉", "重庆小面", "减肥！不吃"
    };
    private String[] mDineTogetherArray = new String[]{
            "火锅", "烧烤自助", "酸菜鱼", "日料", "东北菜", "江浙菜", "川菜"
    };

    private String[] mFoodArr = null;

    private boolean isStartChoose = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleRightIv = findViewById(R.id.iv_title_ok);
        mWorkingLunchBtn = findViewById(R.id.rb_working_lunch);
        mDineTogetherBtn = findViewById(R.id.rb_dine_together);
        mFoodContentTv = findViewById(R.id.tv_food);
        mStartTv = findViewById(R.id.tv_start);
    }

    private void initEvent() {
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWorkingLunchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isStartChoose = false;
                    mFoodArr = mWorkingLunchArray;
                    mFoodContentTv.setText(mFoodArr[0]);
                }
            }
        });

        mDineTogetherBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isStartChoose = false;
                    mFoodArr = mDineTogetherArray;
                    mFoodContentTv.setText(mFoodArr[0]);
                }
            }
        });
        mStartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStartChoose) {
                    isStartChoose = true;
                    new LoopThread().start();
                    mStartTv.setText("停止");
                } else {
                    isStartChoose = false;
                    mStartTv.setText("开始");
                }

            }
        });

    }

    private void initData() {
        mTitleTv.setText("今天吃什么");
        mFoodArr = mWorkingLunchArray;

    }

    private class LoopThread extends Thread {
        @Override
        public void run() {
            while (isStartChoose) {
                if (mFoodArr != null) {
                    String str = mFoodArr[new Random().nextInt(mFoodArr.length)];
                    try {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mWorkingLunchBtn.isChecked()) {
                                    mFoodContentTv.setText("今天吃" + str);
                                } else {
                                    mFoodContentTv.setText("会餐吃" + str);
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedActivity.class);
        context.startActivity(intent);
    }
}
