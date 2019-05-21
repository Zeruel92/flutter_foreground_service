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

## Setup

In order to use this plugin add to your android manifest

``` <uses-permission android:name="android.permission.FOREGROUND_SERVICE"></uses-permission> ```

and

``` <service android:name="it.zeruel.flutter_foreground_service.ForegroundService"></service> ```

## Using
### Starting a foreground service
```
FlutterForegroundService.start(
                    title: 'Titolo',
                    text: 'Testo Notifica',
                    subText: 'Sottotesto',
                    ticker: 'Accessibilità',
                    callback: backgroundtask)
```

backgroundtask must be a global void function

### Stopping a foreground service

```
FlutterForegroundService.stop()
```
