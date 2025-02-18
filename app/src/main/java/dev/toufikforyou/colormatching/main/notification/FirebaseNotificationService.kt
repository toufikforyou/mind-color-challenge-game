package dev.toufikforyou.colormatching.main.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG", "Refreshed token: $token")
    }
}