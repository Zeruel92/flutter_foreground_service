import 'package:flutter/material.dart';
import 'package:flutter_foreground_service/flutter_foreground_service.dart';

void main() => runApp(MyApp());

void backgroundtask() => print('This run in background ${DateTime.now()}');

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
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
                    text: 'Testo Notifica',
                    subText: 'Sottotesto',
                    ticker: 'Accessibilità',
                    callback: backgroundtask,
                    seconds: 10),
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
