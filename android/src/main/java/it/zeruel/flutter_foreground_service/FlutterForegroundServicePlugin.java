package it.zeruel.flutter_foreground_service;

import android.content.Context;
import android.content.Intent;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;


/** FlutterForegroundServicePlugin */
public class FlutterForegroundServicePlugin implements MethodCallHandler {
  /** Plugin registration. */

  private final Context context;
  public static  MethodChannel channel;
  public static MethodChannel background;



  public FlutterForegroundServicePlugin(Context context) {
    this.context = context;
  }

  public static void registerWith(Registrar registrar) {
    FlutterForegroundServicePlugin plugin = new FlutterForegroundServicePlugin(registrar.context());
    channel = new MethodChannel(registrar.messenger(), "flutter_foreground_service");
    background = new MethodChannel(registrar.messenger(), "flutter_foreground_service_background");
    channel.setMethodCallHandler(plugin);

    ForegroundService.setMethodChannel(background);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {

    switch (call.method){
      case "start": {
        Intent intent = new Intent(context, ForegroundService.class);
        intent.setAction("start");
        intent.putExtra("title", (String)call.argument("title"));
        intent.putExtra("text", (String)call.argument("text"));
        intent.putExtra("subText", (String)call.argument("subText"));
        intent.putExtra("ticker", (String)call.argument("ticker"));
        intent.putExtra("handle", (long) call.argument("callback"));
        intent.putExtra("timeout",((int) call.argument("timeout") * 1000));
        context.startService(intent);
        result.success(null);
        break;
      }
      case "stop":{
        Intent intent = new Intent(context, ForegroundService.class);
        intent.setAction("stop");
        context.stopService(intent);
        break;
      }
      default: {
        result.notImplemented();
        break;
      }
    }
  }
}
