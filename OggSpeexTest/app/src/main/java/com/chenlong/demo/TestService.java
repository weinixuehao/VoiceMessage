package com.chenlong.demo;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import jigao.oggspeextest.ITestService;
import jigao.oggspeextest.R;

/**
 * Created by user on 16/9/26.
 */
public class TestService extends Service {
    private final static String TAG = TestService.class.getSimpleName();
    private TestServiceBinder testServiceBinder;

    private class TestServiceBinder extends ITestService.Stub {

        @Override
        public boolean print(String value) throws RemoteException {
            Log.d(TAG, "value:" + value);
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return testServiceBinder;
    }

    private Notification getForegroundServiceNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.door_lock_icon_1)
                        .setContentTitle("常驻服务")
                        .setOngoing(true)
//                        .setContentIntent(PendingIntent.getActivity(this, 0, enableBtIntent,
//                                PendingIntent.FLAG_CANCEL_CURRENT))
                        .setContentText("如果该通知不可见，无法进行开门。");
        return mBuilder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "onStartCommand..!");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(TAG,"executed at " + new Date().toString());
//            }
//        }).start();
//
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int offset= 10 * 1000;//间隔时间10s
//        long triggerAtTime = SystemClock.elapsedRealtime() + offset;
//        Intent i = new Intent(this, BaseReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
//        }
//        manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);

        return START_STICKY;
//        BaseReceiver.completeWakefulIntent(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate..!");
        testServiceBinder = new TestServiceBinder();
        startForeground(1500, getForegroundServiceNotification());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLoaMemory..!");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved..!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy..!");
    }
}
