package it.zeruel.flutter_foreground_service_example;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.app.FlutterApplication;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.GeneratedPluginRegistrant;
import it.zeruel.flutter_foreground_service.FlutterForegroundServicePlugin;
import it.zeruel.flutter_foreground_service.ForegroundService;

public class MainActivity extends FlutterApplication implements PluginRegistry.PluginRegistrantCallback {
  @Override
  public void onCreate() {
    super.onCreate();
      ForegroundService.setPluginRegistrant(this);
    //GeneratedPluginRegistrant.registerWith(this);
  }

    @Override
    public void registerWith(PluginRegistry pluginRegistry) {
        GeneratedPluginRegistrant.registerWith(pluginRegistry);
    }
}
