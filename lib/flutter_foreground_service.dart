import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';





void init() {
  print('creo il background channel');
  const MethodChannel _backgroundChannel =
  const MethodChannel('flutter_foreground_service_background');
  WidgetsFlutterBinding.ensureInitialized();
  _backgroundChannel
      .setMethodCallHandler((MethodCall call) async {
    if (call.method == 'trigger') {
      print('bb');
      final dynamic args = call.arguments;
      final CallbackHandle handle =
          CallbackHandle.fromRawHandle(args['handle']);
      final Function closure = PluginUtilities.getCallbackFromHandle(handle);
      closure();
    }
  });

}

class FlutterForegroundService {

  static const MethodChannel _channel =
      const MethodChannel('flutter_foreground_service');

  static Future<String> start(
      {String title,
      String text,
      String subText,
      String ticker,
      dynamic Function() callback,
      int seconds}) {
    final args = {
      'title': title,
      'text': text,
      'subText': subText,
      'ticker': ticker,
      'callback': PluginUtilities.getCallbackHandle(callback).toRawHandle(),
      'initCallback': PluginUtilities.getCallbackHandle(init).toRawHandle(),
      'timeout': seconds
    };

    return _channel.invokeMethod('start', args);
  }

  static Future<void> stop() => _channel.invokeMethod('stop');
}
