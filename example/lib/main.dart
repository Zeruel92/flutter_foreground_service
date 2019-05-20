import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_foreground_service/flutter_foreground_service.dart';
import 'package:http/http.dart' as http;

void main() => runApp(MyApp());

//void backgroundtask() => print('${DateTime.now()}');
void backgroundtask() => http.post('http://192.168.1.215:8080',
    body: json.encode({"data": "${DateTime.now()}", "telefono": "p10lite"}));

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    FlutterForegroundService.init();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              RaisedButton(
                child: Text('Avvia'),
                onPressed: () => FlutterForegroundService.start(
                    title: 'Titolo',
                    text: 'Prova',
                    subText: 'Scanning in backostia',
                    ticker: 'aa',
                    callback: backgroundtask),
              ),
              RaisedButton(
                child: Text('Stop'),
                onPressed: () => FlutterForegroundService.stop(),
              )
            ],
          ),
        ),
      ),
    );
  }
}
