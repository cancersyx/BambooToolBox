package com.zsf.toolbox.mirror;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.zsf.toolbox.R;

/**
 * Created by EWorld
 * 2022/5/31
 */
public class MirrorActivity extends AppCompatActivity {
    private static final String TAG = "MirrorActivity";
    private LinearLayout mContainer;
    private Camera mCamera;
    private Preview mPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
        mContainer = findViewById(R.id.ll_container);

        mPreview = new Preview(this);
        mContainer.addView(mPreview);

        safeCameraOpen(0);
        mPreview.setCamera(mCamera);

    }

    private boolean safeCameraOpen(int cameraId) {
        boolean qOpened = false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(cameraId);
            qOpened = (mCamera != null);
            Log.d(TAG, ">>>>>> qOpened = " + qOpened);
        } catch (Exception e) {
            Log.e(TAG, "failed to open Camera");
            e.printStackTrace();
        }
        return qOpened;
    }

    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MirrorActivity.class);
        context.startActivity(intent);
    }
}
