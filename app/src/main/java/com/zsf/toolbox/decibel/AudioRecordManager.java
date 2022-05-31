package com.zsf.toolbox.decibel;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author EWorld  e-mail:852333743@qq.com
 * 2020/1/13
 */
public class AudioRecordManager {
    private static final String TAG = "AudioRecordManager";
    private static final int SAMPLE_RATE_IN_HZ = 8000;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
            AudioFormat.ENCODING_PCM_16BIT);
    private AudioRecord mAudioRecord;
    private Handler mHandler;
    private int mWhat;
    private Object mLock;
    public static boolean isGetVoiceRun;

    public AudioRecordManager(Handler handler, int what) {
        mHandler = handler;
        this.mWhat = what;
        mLock = new Object();
    }

    public void getNoiseLevel() {
        if (isGetVoiceRun) {
            //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "---------还在录音呢--------------");
            return;
        }

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        if (mAudioRecord == null) {
            Log.d(TAG, ">>>>>> mAudioRecord 初始化失败");
        }
        isGetVoiceRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioRecord.startRecording();
                short[] buffer = new short[BUFFER_SIZE];
                while (isGetVoiceRun) {
                    int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                    long v = 0;
                    for (int i = 0; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    double mean = v / r;
                    double volume = 10 * Math.log10(mean);
                    // TODO: 2020/1/13 锁
                    synchronized (mLock) {
                        try {
                            mLock.wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Message message = Message.obtain();
                    message.what = mWhat;
                    message.obj = volume;
                    mHandler.sendMessage(message);
                }
                //话筒对象释放
                if (null != mAudioRecord){
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                }

            }
        }).start();
    }
}
