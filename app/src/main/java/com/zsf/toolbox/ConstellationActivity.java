package com.zsf.toolbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by EWorld
 * 2022/6/13
 * 二十八星宿
 */
public class ConstellationActivity extends AppCompatActivity {
    private ImageView mBackIv;
    private TextView mTitleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation);
    }
}
