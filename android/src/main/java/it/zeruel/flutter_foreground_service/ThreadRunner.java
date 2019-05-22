package it.zeruel.flutter_foreground_service;


import android.content.Context;
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
    private Thread t;
    private Map<String, Long> arg;
    private boolean running;
    private int timeout;
    private MethodChannel methodChannel;
    private Context ctx;

    public ThreadRunner(Context ctx, Map<String, Long> arg, int timeout, MethodChannel backgroundChannel, PluginRegistry.PluginRegistrantCallback pluginRegistrantCallback){
        this.t = new Thread(this);
        this.arg = arg;
        running = true;
        this.timeout = timeout;
        this.methodChannel = backgroundChannel;
        this.pluginRegistrantCallback = pluginRegistrantCallback;

        FlutterMain.ensureInitializationComplete(ctx, null);
        FlutterRunArguments args;
        FlutterCallbackInformation callbackInfo;
        long handle = arg.get("handle");
        long init = arg.get("init");
        sBackgroundFlutterView = new FlutterNativeView(ctx, true);
        args = new FlutterRunArguments();
        callbackInfo = FlutterCallbackInformation.lookupCallbackInformation(init);
        args.bundlePath = FlutterMain.findAppBundlePath(ctx);
        args.entrypoint = callbackInfo.callbackName;
        args.libraryPath = callbackInfo.callbackLibraryPath;
        sBackgroundFlutterView.runFromBundle(args);
        this.pluginRegistrantCallback.registerWith(sBackgroundFlutterView.getPluginRegistry());

        this.t.start();
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        while(running){
            try {
                Log.d("a","a");
                this.t.sleep(this.timeout);
                methodChannel.invokeMethod("trigger",arg);
            } catch (InterruptedException e) {
                e.printStackTrace();
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