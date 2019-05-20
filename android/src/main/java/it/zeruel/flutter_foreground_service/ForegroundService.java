package it.zeruel.flutter_foreground_service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

public class ForegroundService extends Service  {

//    public ForegroundService(Context context){
//        this.context=context;
//    }



    private static final int ONGOING_NOTIFICATION_ID = 1;
    private String CHANNEL_ID = "flutter_foreground_service_channel_id";
    private ThreadRunner t;
//    private Context context;

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = "flutter_foreground_service";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("start")) {
            PackageManager pm = getApplicationContext().getPackageManager();
            Intent notificationIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Bundle bundle = intent.getExtras();
            long handle = bundle.getLong("handle");

            Notification notification =
                    new NotificationCompat.Builder(this,CHANNEL_ID)
                            .setOngoing(true)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentTitle(bundle.getString("title"))
                            .setContentText(bundle.getString("text"))
                            .setSubText(bundle.getString("subText"))
                            .setTicker(bundle.getString("ticker"))
                            .setSmallIcon(android.R.drawable.ic_notification_overlay)
                            .setContentIntent(pendingIntent)
                            .build();
            startForeground(ONGOING_NOTIFICATION_ID, notification);
            final Map<String, Long> arg = new HashMap<String,Long>();
            arg.put("handle",handle);

            FlutterNativeView sBackgroundFlutterView = new FlutterNativeView(getApplicationContext(), true);
            FlutterRunArguments args = new FlutterRunArguments();
            FlutterCallbackInformation callbackInfo = FlutterCallbackInformation.
                    lookupCallbackInformation(handle);
            args.bundlePath = FlutterMain.findAppBundlePath(getApplicationContext());
            args.entrypoint = callbackInfo.callbackName;
            args.libraryPath = callbackInfo.callbackLibraryPath;
            sBackgroundFlutterView.runFromBundle(args);
            final MethodChannel mBackgroundChannel = new MethodChannel(sBackgroundFlutterView,
                   "flutter_foreground_service_background");
           // mBackgroundChannel.setMethodCallHandler();
           t = new ThreadRunner(arg,mBackgroundChannel);
//            Handler handler = new Handler();
//            while(true) {
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        mBackgroundChannel.invokeMethod("trigger", arg);
//                    }
//                }, 5000);
//            }
        }
        else{
            //t.stop();
            stopSelf();
            stopForeground(true);
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}