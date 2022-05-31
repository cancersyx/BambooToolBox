package com.zsf.toolbox.mirror;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.List;

/**
 * Created by EWorld
 * 2022/5/31
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "Preview";
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera mCamera;

    public Preview(Context context) {
        this(context, null);
    }

    public Preview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Preview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        surfaceView = new SurfaceView(context);


        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setPreviewSize(previewSize.width, previewSize.height);
        parameters.setPreviewSize(300, 500);
        requestLayout();
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) {
            return;
        }
        stopPreviewAndFreeCamera();
        mCamera = camera;
        if (mCamera != null) {
            List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            //supportedPreviewSizes = localSizes; todo
            requestLayout();

            try {
                Log.d(TAG, ">>>>>>789");
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    }

    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
