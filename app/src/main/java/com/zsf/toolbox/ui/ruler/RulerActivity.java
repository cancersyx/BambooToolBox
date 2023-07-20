package com.zsf.toolbox.ui.ruler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsf.toolbox.R;


/**
 * Created by EWorld
 * 2021/11/29
 */
public class RulerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保持屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_ruler);
        //获取屏幕信息
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        RulerView rulerView = new RulerView(this, displayMetrics);
        LinearLayout container = findViewById(R.id.ll_container);
        container.addView(rulerView);

        TextView title = findViewById(R.id.tv_title);
        title.setText("尺子");

        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RulerActivity.class);
        context.startActivity(intent);
    }
}
