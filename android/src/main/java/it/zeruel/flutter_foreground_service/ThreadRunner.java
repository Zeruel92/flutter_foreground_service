package it.zeruel.flutter_foreground_service;

import android.content.Context;
import java.util.Map;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

public class ThreadRunner implements Runnable {

    private Thread t;
    private Map<String, Long> arg;
    private Context context;
    private boolean running;
    private int timeout;
    private PluginRegistry.PluginRegistrantCallback pluginRegistrantCallback;

    public ThreadRunner(Context ctx, Map<String, Long> arg, int timeout, PluginRegistry.PluginRegistrantCallback pluginRegistrantCallback){
        this.t = new Thread(this);
        this.arg = arg;
        this.context = ctx;
        running = true;
        this.timeout = timeout;
        this.t.start();
        this.pluginRegistrantCallback = pluginRegistrantCallback;
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        FlutterNativeView sBackgroundFlutterView;
        FlutterRunArguments args;
        FlutterCallbackInformation callbackInfo;
        MethodChannel mBackgroundChannel;
        while(running){
            try {
                this.t.sleep(this.timeout);
                long handle = arg.get("handle");

                 sBackgroundFlutterView = new FlutterNativeView(context, true);
                 args = new FlutterRunArguments();
                callbackInfo = FlutterCallbackInformation.lookupCallbackInformation(handle);
                args.bundlePath = FlutterMain.findAppBundlePath(context);
                args.entrypoint = callbackInfo.callbackName;
                args.libraryPath = callbackInfo.callbackLibraryPath;

                mBackgroundChannel = new MethodChannel(sBackgroundFlutterView,
                        "flutter_foreground_service_background");

                sBackgroundFlutterView.runFromBundle(args);
                this.pluginRegistrantCallback.registerWith(sBackgroundFlutterView.getPluginRegistry());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
