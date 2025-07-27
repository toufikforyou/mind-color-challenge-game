package dev.toufikforyou.colormatching.main.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import dev.toufikforyou.colormatching.MainActivity
import dev.toufikforyou.colormatching.R
import java.util.Calendar

class NotificationHelper(
    private val context: Context, private val permissionHandler: NotificationPermissionHandler
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        // Game notifications channel (for high scores, etc.)
        val gameChannel = NotificationChannel(
            GAME_CHANNEL_ID, "Game Notifications", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications from Color Matching Game"
            enableLights(true)
            enableVibration(true)
        }

        // Daily reminder channel
        val reminderChannel = NotificationChannel(
            REMINDER_CHANNEL_ID, "Daily Reminders", NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Daily reminders to play Color Matching Game"
            enableLights(true)
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(gameChannel)
        notificationManager.createNotificationChannel(reminderChannel)
    }

    private fun showGameNotification(
        title: String, message: String, channelId: String = GAME_CHANNEL_ID
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId).setContentTitle(title)
            .setContentText(message).setSmallIcon(R.drawable.color_matching_game_icon)
            .setAutoCancel(true).setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()

        notificationManager.notify(
            if (channelId == REMINDER_CHANNEL_ID) REMINDER_NOTIFICATION_ID else GAME_NOTIFICATION_ID,
            notification
        )
    }

    fun scheduleDailyReminder(hour: Int, minute: Int) {
        // Check all required permissions
        if (!permissionHandler.hasRequiredPermissions()) {
            return
        }

        // Check if reminder channel is enabled
        if (!isReminderChannelEnabled()) {
            return
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }

        }

        val intent = Intent(context, DailyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
        )
        // Normal daily reminder
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )

    }

//    fun cancelDailyReminder() {
//        val intent = Intent(context, DailyReminderReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context, REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE
//        )
//        alarmManager.cancel(pendingIntent)
//    }

    fun isReminderChannelEnabled(): Boolean {
        val channel = notificationManager.getNotificationChannel(REMINDER_CHANNEL_ID)
        return channel?.importance != NotificationManager.IMPORTANCE_NONE
    }

    fun openNotificationSettings() {
        // If alarm permission is not granted, open alarm settings first
        if (!permissionHandler.checkAlarmPermission()) {
            permissionHandler.openAlarmSettings()
            return
        }

        // Otherwise, open notification channel settings
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, REMINDER_CHANNEL_ID)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    companion object {
        private const val GAME_CHANNEL_ID = "game_notification_channel"
        private const val REMINDER_CHANNEL_ID = "daily_reminder_channel"
        private const val GAME_NOTIFICATION_ID = 1
        private const val REMINDER_NOTIFICATION_ID = 2
        private const val REMINDER_REQUEST_CODE = 2001
    }

    class DailyReminderReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notificationHelper =
                NotificationHelper(context, NotificationPermissionHandler(context))
            if (notificationHelper.isReminderChannelEnabled()) {
                notificationHelper.showGameNotification(
                    "Time to Play!",
                    "Challenge yourself with new color matching puzzles!",
                    REMINDER_CHANNEL_ID
                )
            }
        }
    }
} 