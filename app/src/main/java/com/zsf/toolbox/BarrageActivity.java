package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.barrage.CaptionTextView;

/**
 * Created by EWorld
 * 2022/5/27
 * 滚动字幕
 */
public class BarrageActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "BarrageActivity";
    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mOkIv;

    private EditText mInputEt;
    private CaptionTextView mMarqueeTv;
    private String mContent;
    private boolean isRunning = false;

    private SeekBar mSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_barrage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText("滚动字幕");
        mOkIv = findViewById(R.id.iv_title_ok);
        mOkIv.setVisibility(View.VISIBLE);
        mInputEt = findViewById(R.id.et_input);
        mMarqueeTv = findViewById(R.id.tv_barrage_content);
        mSeekBar = findViewById(R.id.seek_bar);

        mBackIv.setOnClickListener(this);
        mOkIv.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mMarqueeTv.stopMarquee();
                finish();
                break;
            case R.id.iv_title_ok:
                mContent = mInputEt.getText().toString();
                if (mContent.isEmpty()) {
                    Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isRunning) {
                    mInputEt.setVisibility(View.GONE);
                    mMarqueeTv.setVisibility(View.VISIBLE);
                    mSeekBar.setVisibility(View.VISIBLE);
                    mMarqueeTv.setContent(mContent);
                    mMarqueeTv.startMarquee();
                    mOkIv.setImageResource(R.drawable.icon_restart);
                    isRunning = true;
                } else {
                    mMarqueeTv.setVisibility(View.GONE);
                    mSeekBar.setVisibility(View.GONE);
                    mInputEt.setVisibility(View.VISIBLE);
                    mInputEt.setText("");
                    mOkIv.setImageResource(R.drawable.icon_ok);
                    isRunning = false;
                    mMarqueeTv.stopMarquee();
                }
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, ">>>>>>> progress = " + progress);
        int value = progress + 180;
        mMarqueeTv.setTextSize(value);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, ">>>>>>> onStartTrackingTouch = ");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, ">>>>>>> onStopTrackingTouch = ");
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BarrageActivity.class);
        context.startActivity(intent);
    }
}
