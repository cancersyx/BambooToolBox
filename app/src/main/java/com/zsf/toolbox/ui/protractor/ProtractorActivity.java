package com.zsf.toolbox.ui.protractor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zsf.toolbox.R;

import java.io.IOException;

/**
 * Created by EWorld
 * 2022/5/31
 * 量角尺
 */
public class ProtractorActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private Camera mCamera = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protractor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mSurfaceView = findViewById(R.id.surface_view);

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopPreview();
            }
        });
    }

    private void startPreview() {
        try {
            mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mSurfaceView.getHolder());
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ProtractorActivity.class);
        context.startActivity(intent);
    }
}
