import 'dart:async';

import 'package:flutter/services.dart';

class FlutterGp {
  static const MethodChannel _channel =
      const MethodChannel('flutter_gp');

  static Future<String> get GetGuid async {
    final String version = await _channel.invokeMethod('GetGuid');
    return version;
  }
}
