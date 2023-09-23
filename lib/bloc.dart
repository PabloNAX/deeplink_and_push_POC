import 'dart:async';

import 'package:flutter/services.dart';

abstract class Bloc {
  void dispose();
}

class DeepLinkBloc extends Bloc {
  //Event Channel creation
  static const stream = const EventChannel('poc.com.example.deeplink/events');

  //Method channel creation
  static const platform =
      const MethodChannel('poc.com.example.deeplink/channel');

  StreamController<String> _stateController = StreamController();

  Stream<String> get state => _stateController.stream;

  Sink<String> get stateSink => _stateController.sink;

  //Adding the listener into contructor
  DeepLinkBloc() {
    print('DeepLinkBloc created');
    startUri().then((uri) {
      print('Initial link: $uri');
      _onRedirected(uri);
    });
    stream.receiveBroadcastStream().listen((uri) {
      print('Received link: $uri');
      _onRedirected(uri);
    });
  }

  void _onRedirected(String? uri) {
    print('Redirected to: $uri');
    if (uri != null) {
      stateSink.add(uri);
    }
  }

  @override
  void dispose() {
    _stateController.close();
  }

  Future<String?> startUri() async {
    try {
      return platform.invokeMethod('initialLink');
    } on PlatformException catch (e) {
      return "Failed to Invoke: '${e.message}'.";
    }
  }
}
