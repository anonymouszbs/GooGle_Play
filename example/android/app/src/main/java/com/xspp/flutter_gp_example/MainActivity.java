package com.xspp.flutter_gp_example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  String guid = "吹牛逼";
  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);
    getGuid();
    initEvent(flutterEngine);
    initMatheod(flutterEngine);
  }
  private void initEvent(FlutterEngine flutterEngine){
    new EventChannel(flutterEngine.getDartExecutor(), "com.xspp.flutter_gp_example/event").
            setStreamHandler(new EventChannel.StreamHandler() {

              private BroadcastReceiver chargingStateChangeReceiver;

              @Override
              public void onListen(Object args, final EventChannel.EventSink events) {
                chargingStateChangeReceiver = createChargingStateChangeReceiver(events);
                registerReceiver(
                        chargingStateChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                // Log.w(TAG, "adding listener");
              }

              @Override
              public void onCancel(Object args) {
                unregisterReceiver(chargingStateChangeReceiver);
                chargingStateChangeReceiver = null;
                // Log.w(TAG, "cancelling listener");
              }}
              );
  }
  private void initMatheod(FlutterEngine flutterEngine){
    new MethodChannel(flutterEngine.getDartExecutor(), "flutter_gp").setMethodCallHandler(
            (MethodCall methodCall, MethodChannel.Result result) -> {
              if (methodCall.method.equals("GetGuid")) {
                result.success(guid);
              }
            }
    );


  };
  public void getGuid(){
    new Thread(new Runnable() {
      String advertisingId;
      public void run() {
        try {
          AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient
                  .getAdvertisingIdInfo(MainActivity.this);
          advertisingId = adInfo.getId();
          guid = advertisingId;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private BroadcastReceiver createChargingStateChangeReceiver(final EventChannel.EventSink events) {
    return new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {

        try {
          Bundle extras = intent.getExtras();
          if (extras != null) {
            String referrer = extras.getString("referrer");
            events.success(referrer);
          }else{}
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
  }
}
