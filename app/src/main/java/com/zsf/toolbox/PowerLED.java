package com.zsf.toolbox;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by EWorld
 * 2021/11/29
 * <p>
 * android6.0以下使用Camera，6.0以上则使用CameraManager
 */
public class PowerLED {
    private static final String TAG = "PowerLED";
    private Camera mCamera;
    private boolean isOn;
    private CameraManager mCameraManager = null;

    public PowerLED(Activity activity) {
        this.isOn = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                try {
                    mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            try {
                mCamera = Camera.open();
                Log.d(TAG, ">>>>>>> mCamera = " + mCamera);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void turnOn() {
        try {
            if (!isOn) {
                isOn = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode("0", true);//打开闪光灯
                } else {
                    if (mCamera != null) {
                        Camera.Parameters parameters = mCamera.getParameters();
                        Log.d(TAG, ">>>>>>> turnOn(), parameters = " + parameters);
                        if (parameters != null) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启闪光灯
                            mCamera.setParameters(parameters);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOff() {
        try {
            if (isOn) {
                isOn = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode("0", false);//打开闪光灯
                } else {
                    if (mCamera != null) {
                        Camera.Parameters parameters = mCamera.getParameters();
                        Log.d(TAG, ">>>>>>> turnOff(), parameters = " + parameters);
                        if (parameters != null) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭闪光
                            mCamera.setParameters(parameters);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
