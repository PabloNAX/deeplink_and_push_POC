package com.example.deeplink

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("Message received kt")
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            println("Message data: ${remoteMessage.data}")
            val url = remoteMessage.data["url"]
            // Print the URL
            println("Received URL inside Push.kt: $url")
            // Send a local broadcast with the URL
            val intent = Intent("com.example.deeplink.ON_MESSAGE")
            intent.putExtra("url", url)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }
}