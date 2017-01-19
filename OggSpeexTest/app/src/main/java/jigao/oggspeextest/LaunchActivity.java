package jigao.oggspeextest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chenlong.demo.SecondService;
import com.chenlong.demo.TestService;
import com.gauss.recorder.AudioController;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.view.RecorderButton;

import junit.framework.Test;

import java.io.File;

public class LaunchActivity extends Activity implements SensorEventListener, AudioManager.OnAudioFocusChangeListener {
    private String filename = "";
    private ImageView imageView;
    private final String TAG = LaunchActivity.class.getSimpleName();

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private RecorderButton recorderButton;
    private AudioController audioController;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);


        audioController = AudioController.getInstance(this);
        audioController.init();
        findViewById(R.id.player2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(filename)) {
                    filename = "storage/emulated/0/1473131795520_gauss.spx";
                }
                if (audioController.requestFocus()) {
                    audioController.changeToSpeaker();
                    SpeexPlayer.getInstance().startPlay(filename);
                    SpeexPlayer.getInstance().setOnPlayListener(new SpeexPlayer.OnPlayListener() {
                        @Override
                        public void onPlayDone() {
                            audioController.abandonFocus();
                            Log.i(TAG, "play done:" + Thread.currentThread().getName());
                        }
                    });
                }
            }
        });

        recorderButton = (RecorderButton) findViewById(R.id.recorderButton);
        recorderButton.setOnRecordListener(new RecorderButton.OnRecordListener() {
            @Override
            public void onDone(String path, int duration) {
                filename = path;
                Log.i(TAG, "file len:" + String.valueOf(new File(filename).length()));
                isRecording = false;
            }

            @Override
            public void onUserCancel() {
                isRecording = false;
            }

            @Override
            public void onStartRecord() {
                isRecording = true;
                if (SpeexPlayer.getInstance().isPlaying()) {
                    SpeexPlayer.getInstance().stopPlay();
                }
                Log.i(TAG, "Start....");
            }
        });


//        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity"));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Smart manager not installed on this device", Toast.LENGTH_LONG)
                    .show();
        }
        startService(new Intent(this, TestService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mSensorManager.unregisterListener(this);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isRecording || !SpeexPlayer.getInstance().isPlaying()) {
//            audioController.changeToNormalMode();
            Log.i(TAG, "is recording or is playing.");
            return;
        }

        if (event.values[0] < mSensor.getMaximumRange()) {
            audioController.changeToReceiver();
        } else {
            audioController.changeToSpeaker();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioController.unInit();
        audioController = null;
        stopService(new Intent(this, TestService.class));
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.i(TAG, "focus change:" + focusChange);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "keyCode:"+ keyCode);
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                audioController.raiseVolume();
//            return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                audioController.lowerVolume();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
