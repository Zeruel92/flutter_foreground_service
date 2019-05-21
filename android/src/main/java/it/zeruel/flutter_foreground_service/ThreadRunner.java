package it.zeruel.flutter_foreground_service;

import android.content.Context;
import java.util.Map;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

public class ThreadRunner implements Runnable {

    private Thread t;
    private Map<String, Long> arg;
    private Context context;
    private boolean running;

    public ThreadRunner(Context ctx, Map<String, Long> arg){
        this.t = new Thread(this);
        this.arg = arg;
        this.context = ctx;
        running = true;
        this.t.start();
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
                this.t.sleep(10000);
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

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
