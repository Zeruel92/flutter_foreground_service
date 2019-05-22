import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const MethodChannel _backgroundChannel =
    const MethodChannel('flutter_foreground_service_background');

class FlutterForegroundService {
  static const MethodChannel _channel =
      const MethodChannel('flutter_foreground_service');

  static Future<bool> init() async {
    _backgroundChannel.setMethodCallHandler((MethodCall call) async {
      if (call.method == 'trigger') {
        WidgetsFlutterBinding.ensureInitialized();
        final dynamic args = call.arguments;
        final CallbackHandle handle =
            CallbackHandle.fromRawHandle(args['handle']);
        final Function closure = PluginUtilities.getCallbackFromHandle(handle);
        closure();
      }
    });
    return true;
  }

  static Future<String> start(
      {String title,
      String text,
      String subText,
      String ticker,
        dynamic Function() callback,
        int seconds
      }) {
    final args = {
      'title': title,
      'text': text,
      'subText': subText,
      'ticker': ticker,
      'callback': PluginUtilities.getCallbackHandle(callback).toRawHandle(),
      'timeout': seconds
    };

    return _channel.invokeMethod('start', args);
  }

  static Future<void> stop() => _channel.invokeMethod('stop');
}
