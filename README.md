# flutter_foreground_service

Foreground Service plugin for Flutter (doesn't work on iOS)

This is a draft

## Installing

add in pubspec.yaml

```
    flutter_foreground_service:
        git:
            url: https://github.com/pspgt/flutter_foreground_service.git
            ref: master
```
and import

```
import 'package:flutter_foreground_service/flutter_foreground_service.dart';
```

## Setup

In order to use this plugin add to your android manifest

``` <uses-permission android:name="android.permission.FOREGROUND_SERVICE"></uses-permission> ```

and

``` <service android:name="it.zeruel.flutter_foreground_service.ForegroundService"></service> ```

You need also to create Application.java in order to use plugins in background tasks:

```
import io.flutter.app.FlutterApplication;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugins.GeneratedPluginRegistrant;
import it.zeruel.flutter_foreground_service.ForegroundService;

public class Application extends FlutterApplication implements PluginRegistry.PluginRegistrantCallback {
    @Override
    public void onCreate() {
        super.onCreate();
        ForegroundService.setPluginRegistrant(this);
    }

    @Override
    public void registerWith(PluginRegistry registry) {
        GeneratedPluginRegistrant.registerWith(registry);
    }
}
```

and set on AndroidManifest.xml

```
<application
        android:name=".Application"
```

## Using

### Starting a foreground service

```
FlutterForegroundService.start(
                    title: 'Titolo',
                    text: 'Testo Notifica',
                    subText: 'Sottotesto',
                    ticker: 'Accessibilità',
                    callback: backgroundtask,
                    seconds: 30
                    )
```

backgroundtask must be a global void function

### Stopping a foreground service

```
FlutterForegroundService.stop()
```
