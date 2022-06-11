package com.zsf.toolbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


/**
 * @author EWorld
 * 2020/8/10
 * 放大镜
 */
public class MagnifierActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "MagnifierActivity";
    //用于显示内容
    private SurfaceView mSurfaceView;
    private int mValue = 50;
    //This class was deprecated in API level 21.
    //We recommend using the new android.hardware.camera2 API for new applications.
    private Camera.Parameters mParameters;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private SeekBar mSeekBar;
    //相机ID
    private int mCameraId = 0;
    private boolean mCameraIsOpenFlag = true;//判断相机是否开启
    //转动角度
    private static final int ROTATION = 90;
    private int mZoomMax;

    private ImageView mBackIv;
    private TextView mTitleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifier);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSurfaceView = findViewById(R.id.surface_view);
        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText("放大镜");


        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//兼容旧的API

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //进度
            mValue = progress + 50;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //开始拖动触发
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(TAG, ">>>>>>> mParameters = " + mParameters);
            //停止拖动触发
            if (mParameters != null) {
                mParameters.setZoom(mValue);
                mCamera.setParameters(mParameters);
                mCamera.startPreview();
            }

        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        startCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    /**
     *
     */
    private void startCamera() {
        if (mCameraIsOpenFlag) {
            if (mCamera != null) {
                mCamera.stopPreview();//停止相机服务
                mCamera.release();//释放手机摄像头
                mCamera = null;
            }
        }
        mCamera = Camera.open(mCameraId);
        if (mCamera != null) {
            mCamera.setDisplayOrientation(ROTATION);
            mParameters = mCamera.getParameters();
            //设置对焦模式为持续对焦
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mParameters.setPictureFormat(PixelFormat.JPEG);
            mParameters.set("orientation", "portrait");//解决自定义相机角度问题
            mParameters.setPreviewSize(320, 240);//设置预览照片的大小
            mParameters.setRotation(ROTATION);
            mZoomMax = mValue;
            mParameters.setZoom(mZoomMax);
            mCamera.setParameters(mParameters);
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                mCameraIsOpenFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
                mCamera.release();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
            mCameraIsOpenFlag = false;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MagnifierActivity.class);
        context.startActivity(intent);
    }


}
