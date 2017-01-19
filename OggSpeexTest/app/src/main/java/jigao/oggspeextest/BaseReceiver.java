package jigao.oggspeextest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.chenlong.demo.TestService;

/**
 * Created by chenlong on 16/9/26.
 */
public class BaseReceiver extends WakefulBroadcastReceiver {
    private final static String TAG = BaseReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive..");
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock cpuWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
//        cpuWakeLock.acquire();
//        context.startService(new Intent(context, TestService.class));
//        cpuWakeLock.release();
//        startWakefulService(context, new Intent(context, TestService.class));
        context.startService(new Intent(context, TestService.class));
    }
}
