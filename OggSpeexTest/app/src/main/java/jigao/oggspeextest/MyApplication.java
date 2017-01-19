package jigao.oggspeextest;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

/**
 * APP的Application
 * Created by DevWiki on 2015/9/16 0016.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

//        Intent intent = new Intent(this, BaseReceiver.class);
//        AlarmManager mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        PendingIntent mPIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, 60000L, mPIntent);
        Log.i(MyApplication.class.getSimpleName(), "oncreate ");
    }

    /**
     * 获取APP的Context方便其他地方调用
     * @return
     */
    public static Context getContext(){
        return context;
    }
}
