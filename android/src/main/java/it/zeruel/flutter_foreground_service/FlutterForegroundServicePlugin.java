package it.zeruel.flutter_foreground_service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

/** FlutterForegroundServicePlugin */
public class FlutterForegroundServicePlugin implements MethodCallHandler {
  /** Plugin registration. */
  ForegroundService service;

  private final Context context;
  public static  MethodChannel channel;
  public static MethodChannel backgroundChannel;

  public FlutterForegroundServicePlugin(Context context) {
    this.context = context;
  }

  public static void registerWith(Registrar registrar) {
    FlutterForegroundServicePlugin plugin = new FlutterForegroundServicePlugin(registrar.context());
    channel = new MethodChannel(registrar.messenger(), "flutter_foreground_service");
    backgroundChannel = new MethodChannel(registrar.messenger(), "flutter_foreground_service_background");
    channel.setMethodCallHandler(plugin);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if(call.method.equals("start")){
      Intent intent = new Intent(context, ForegroundService.class);
      intent.setAction("start");
      intent.putExtra("title", (String)call.argument("title"));
      intent.putExtra("text", (String)call.argument("text"));
      intent.putExtra("subText", (String)call.argument("subText"));
      intent.putExtra("ticker", (String)call.argument("ticker"));
      intent.putExtra("handle", (long) call.argument("callback"));
      //ForegroundService.setChannel(channel);
      context.startService(intent);
      result.success(null);
    }else if(call.method.equals("stop")){
      Intent intent = new Intent(context, ForegroundService.class);
      intent.setAction("stop");
      context.stopService(intent);
    }
//    else if(call.method.equals("init")){
//      ForegroundService service = new ForegroundService(context);
//
//    }
    else {
      result.notImplemented();
    }
  }
}
