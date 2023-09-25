package com.example.deeplink

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import com.google.firebase.messaging.FirebaseMessaging
import android.widget.Toast

class MainActivity: FlutterActivity() {
    private val CHANNEL = "poc.com.example.deeplink/channel"
    private var startString: String? = null
    private val EVENTS = "poc.com.example.deeplink/events"
    private var linksReceiver: BroadcastReceiver? = null
    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val url = intent.getStringExtra("url")
            // Send the URL to Flutter
            flutterEngine?.dartExecutor?.let { MethodChannel(it.binaryMessenger, CHANNEL).invokeMethod("onMessage", url) }
        }
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "initialLink") {
                if (startString != null) {
                    result.success(startString)
                }
            }
        }

        // Register the local broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("com.example.deeplink.ON_MESSAGE"))

        // Get FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("Fetching FCM registration token failed")
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Print the token
            println("FCM Token: $token")

            // Display a toast message with the token
            Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_LONG).show()
        }

        EventChannel(flutterEngine.dartExecutor, EVENTS).setStreamHandler(
            object : EventChannel.StreamHandler {
                override fun onListen(args: Any?, events: EventSink) {
                    linksReceiver = createChangeReceiver(events)
                }

                override fun onCancel(args: Any?) {
                    linksReceiver = null
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = getIntent()
        startString = intent.data?.toString()
    }



    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action === Intent.ACTION_VIEW) {
            linksReceiver?.onReceive(this.applicationContext, intent)
        }
    }

    private fun createChangeReceiver(events: EventSink): BroadcastReceiver? {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) { // NOTE: assuming intent.getAction() is Intent.ACTION_VIEW
                val dataString = intent.dataString ?:
                events.error("UNAVAILABLE", "Link unavailable", null)
                events.success(dataString)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the local broadcast receivers
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        linksReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
    }
}