package com.example.deeplink

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
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
            // Create a notification
            createNotification(url)
        }
    }

    private fun createNotification(url: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        }

        val channelId = "Default"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Replace with your app's notification icon
            .setContentTitle("New Message")
            .setContentText("Received URL: $url")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(0, builder.build())
    }
}