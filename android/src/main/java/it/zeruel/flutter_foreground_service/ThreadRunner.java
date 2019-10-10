package it.zeruel.flutter_foreground_service;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Map;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;


public class ThreadRunner implements Runnable {

    private FlutterNativeView sBackgroundFlutterView;
    private final PluginRegistry.PluginRegistrantCallback pluginRegistrantCallback;
    private static Thread t;
    private final Map<String, Long> arg;
    private boolean running;
    private int timeout;
    private Handler handler;

    public ThreadRunner(Context ctx, Map<String, Long> arg1, int timeout, PluginRegistry.PluginRegistrantCallback pluginRegistrantCallback){
        this.t = new Thread(this);
        this.arg = arg1;
        running = true;
        this.timeout = timeout;

        this.pluginRegistrantCallback = pluginRegistrantCallback;

        FlutterMain.ensureInitializationComplete(ctx, null);
        FlutterRunArguments args;
        FlutterCallbackInformation callbackInfo;
        long handle = arg1.get("handle");
        long init = arg1.get("init");
        sBackgroundFlutterView = new FlutterNativeView(ctx, true);
        args = new FlutterRunArguments();
        callbackInfo = FlutterCallbackInformation.lookupCallbackInformation(init);
        args.bundlePath = FlutterMain.findAppBundlePath(ctx);
        args.entrypoint = callbackInfo.callbackName;
        args.libraryPath = callbackInfo.callbackLibraryPath;
        sBackgroundFlutterView.runFromBundle(args);
        this.pluginRegistrantCallback.registerWith(sBackgroundFlutterView.getPluginRegistry());

        //Fix Issue #2 from weiyinqing
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //Log.e("this is hander message",msg.what+"_____________________________");
            ForegroundService.backgroundChannel.invokeMethod("trigger",arg);
            }
        };
    }

    public static void onInitComplete(){
        t.start();
    }

    public void stop(){
        Log.i("ThreadRunner","stopping thread");
        running = false;
        sBackgroundFlutterView.destroy();
        try{
            this.t.interrupt();
        }catch(Exception ex){
            Log.d("ThreadRunner","Thread Interrupted");
        }
    }

    @Override
    public void run() {
        while(running){
            try {
                this.t.sleep(this.timeout);
                //ForegroundService.backgroundChannel.invokeMethod("trigger",arg);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                Log.d("ThreadRunner","Thread Interrupted");
            }
        }
    }

    public boolean setBackgroundFlutterView(FlutterNativeView view) {
        if (sBackgroundFlutterView != null && sBackgroundFlutterView != view) {
            Log.i("Foreground", "setBackgroundFlutterView tried to overwrite an existing FlutterNativeView");
            return false;
        }
        sBackgroundFlutterView = view;
        return true;
    }
}