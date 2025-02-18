package dev.toufikforyou.colormatching.main.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.toufikforyou.colormatching.MainActivity
import dev.toufikforyou.colormatching.R

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // You can send this token to your server
        // For now, just logging it
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Get notification data
        val title = message.notification?.title ?: "Color Matching"
        val body = message.notification?.body ?: "New challenge waiting for you!"

        // Show notification
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        val channel = NotificationChannel(
            CHANNEL_ID, "Game Notifications", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications from Color Matching Game"
            enableLights(true)
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        // Create intent for notification tap action
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(title).setContentText(body)
                .setSmallIcon(R.drawable.color_matching_game_icon).setAutoCancel(true)
                .setContentIntent(pendingIntent).setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

        // Show notification
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TAG = "FirebaseNotificationService"
        private const val CHANNEL_ID = "color_matching_channel"
        private const val NOTIFICATION_ID = 1
    }
}