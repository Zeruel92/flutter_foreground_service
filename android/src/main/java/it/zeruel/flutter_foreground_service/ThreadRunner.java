package it.zeruel.flutter_foreground_service;

import android.util.Log;

import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class ThreadRunner implements Runnable {

    private Thread t;
    private Map<String, Long> arg;
    private MethodChannel mBackgroundChannel;
    private boolean running;

    public ThreadRunner(Map<String, Long> arg,MethodChannel mBackgroundChannel){
        this.t = new Thread(this);
        this.arg = arg;
        this.mBackgroundChannel = mBackgroundChannel;
        running = true;
        this.t.start();
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        while(running){
            try {
                this.t.sleep(1000);

                mBackgroundChannel.invokeMethod("trigger",arg);
                //Log.d("aa","aaaa");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
