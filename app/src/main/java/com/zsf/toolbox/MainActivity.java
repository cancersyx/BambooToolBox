package com.zsf.toolbox;

import android.Manifest;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zsf.toolbox.decibel.DecibelActivity;
import com.zsf.toolbox.exchange.ExchangeRateActivity;
import com.zsf.toolbox.gradienter.GradienterActivity;
import com.zsf.toolbox.mirror.MirrorActivity;
import com.zsf.toolbox.netspeed.NetSpeedActivity;
import com.zsf.toolbox.protractor.ProtractorActivity;
import com.zsf.toolbox.ruler.RulerActivity;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA_MAGNIFIER = 100;
    public static final int REQUEST_CODE_CAMERA_MIRROR = 101;
    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mFlashLightTv, mMagnifierTv, mDecibel;
    private TextView mCompass, mGradienter, mCalculate;
    private TextView mRulerTv, mSOSTv, mMirrorTv;
    private TextView mProtractorTv, mChangeRateTv, mBarrageTv;
    private TextView mNetworkSpeedTv, mMachineInfoTv, mAboutTv;

    private boolean isOpen = false;
    private CameraManager mCameraManager;
    private Camera mCamera;

    //是否开启手电筒判断标示 默认关闭
    private boolean status = false;
    private PowerLED mPowerLED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        }
    }


    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mBackIv.setVisibility(View.INVISIBLE);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText(R.string.app_name);
        mFlashLightTv = findViewById(R.id.tv_flashlight);
        mMagnifierTv = findViewById(R.id.tv_magnifier);
        mDecibel = findViewById(R.id.tv_decibel);
        mCompass = findViewById(R.id.tv_compass);
        mGradienter = findViewById(R.id.tv_gradienter);
        mCalculate = findViewById(R.id.tv_calculate);
        mRulerTv = findViewById(R.id.tv_ruler);
        mSOSTv = findViewById(R.id.tv_sos);
        mMirrorTv = findViewById(R.id.tv_mirror);
        mProtractorTv = findViewById(R.id.tv_protractor);
        mChangeRateTv = findViewById(R.id.tv_change_rate);
        mBarrageTv = findViewById(R.id.tv_barrage);
        mNetworkSpeedTv = findViewById(R.id.tv_network_speed);
        mMachineInfoTv = findViewById(R.id.tv_machine_info);
        mAboutTv = findViewById(R.id.tv_about);
    }

    private void initEvent() {
        mFlashLightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPowerLED = new PowerLED(MainActivity.this);
                mShowRunnable = new ShowRunnable();
                controlFlashLight();
            }
        });

        mMagnifierTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_MAGNIFIER);
                    } else {
                        MagnifierActivity.startActivity(MainActivity.this);
                    }
                } else {
                    MagnifierActivity.startActivity(MainActivity.this);
                }
            }
        });

        mDecibel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecibelActivity.startActivity(MainActivity.this);
            }
        });

        mCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompassActivity.startActivity(MainActivity.this);
            }
        });

        mGradienter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradienterActivity.startActivity(MainActivity.this);
            }
        });

        mCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateActivity.startActivity(MainActivity.this);
            }
        });

        mRulerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RulerActivity.startActivity(MainActivity.this);
            }
        });

        mSOSTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSOS();
            }
        });

        mMirrorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_MIRROR);
                    } else {
                        MirrorActivity.startActivity(MainActivity.this);
                    }
                } else {
                    MirrorActivity.startActivity(MainActivity.this);
                }

            }
        });

        mProtractorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtractorActivity.startActivity(MainActivity.this);
            }
        });

        mChangeRateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeRateActivity.startActivity(MainActivity.this);
            }
        });

        mBarrageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarrageActivity.startActivity(MainActivity.this);
            }
        });

        mNetworkSpeedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetSpeedActivity.startActivity(MainActivity.this);
            }
        });

        mMachineInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemConfigActivity.startActivity(MainActivity.this);
            }
        });

        mAboutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.startActivity(MainActivity.this);
            }
        });

    }


    private void controlFlashLight() {
        if (!isOpen) {
            //打开手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    mCameraManager.setTorchMode("0", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                final PackageManager pm = getPackageManager();
                final FeatureInfo[] features = pm.getSystemAvailableFeatures();
                for (final FeatureInfo f : features) {
                    if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                        if (null == mCamera) {
                            mCamera = Camera.open();
                        }
                        final Camera.Parameters parameters = mCamera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(parameters);
                        mCamera.startPreview();
                    }
                }
            }
            isOpen = true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    mCameraManager.setTorchMode("0", false);
                    mCameraManager = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            }
            isOpen = false;
        }
    }


    //************************************SOS******************************************************START
    private void openSOS() {
        mPowerLED.turnOff();
        status = false;//手电筒关闭
        if (!firstTime) {
            firstTime = true;
            mHandler.postDelayed(mShowRunnable, 50);
        } else {
            firstTime = false;//闪光灯关闭
            mPowerLED.turnOff();//关闭闪光灯
            mHandler.removeCallbacks(mShowRunnable);//删除线程
        }
    }

    private Handler mHandler = new Handler();
    private boolean firstTime = false;// sos闪光灯是否开启判断
    private int warmingcounter = -1;
    //sos 闪烁时间
    private int[] bgflashtime = new int[]{300, 300, 300, // ... S
            300, 300,
            //
            900,
            //
            900, 300, 900, // --- O
            300, 900,
            //
            900,
            //
            300, 300, 300, // ... S
            300, 300,
            //
            2100};
    private ShowRunnable mShowRunnable;

    class ShowRunnable implements Runnable {

        @Override
        public void run() {
            warmingcounter++;
            if (warmingcounter % 2 == 0) {
                mPowerLED.turnOn();// 开启闪光灯
            } else {
                mPowerLED.turnOff();// 关闭闪光灯
            }
            mHandler.postDelayed(this, bgflashtime[warmingcounter % 18]);
        }
    }

    //************************************SOS******************************************************END

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_MAGNIFIER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MagnifierActivity.startActivity(MainActivity.this);
                } else {
                    Toast.makeText(this, "请开启相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_CAMERA_MIRROR:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MirrorActivity.startActivity(MainActivity.this);
                } else {
                    Toast.makeText(this, "请开启相机权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
