package com.openclassrooms.hexagonal.games.data.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.openclassrooms.hexagonal.games.R
import com.openclassrooms.hexagonal.games.ui.MainActivity

/**
 * Service that handles incoming Firebase Cloud Messaging (FCM) push notifications.
 * It processes the received messages and displays notifications to the user.
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationService : FirebaseMessagingService() {

    private val notificationId = 7
    private val notificationTag = "Hexagonal Games"

    /**
     * Called when a new FCM message is received.
     *
     * @param remoteMessage The received remote message containing notification details.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("Nicolas", "NotificationService - onMessageReceived() : New notification received")

        val sharedPreferences = applicationContext.getSharedPreferences(
            "app_prefs",
            android.content.Context.MODE_PRIVATE
        )
        val areNotificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true)

        // If the message contains a notification payload, display it
        if (areNotificationsEnabled) {
            remoteMessage.notification?.let { notification ->
                sendVisualNotification(notification)
            }
        }
    }

    /**
     * Creates and displays a push notification to the user.
     *
     * @param notification The notification payload received from Firebase.
     */
    private fun sendVisualNotification(notification: RemoteMessage.Notification) {
        // Create an intent to open MainActivity when the notification is clicked
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Retrieve the default notification channel ID from resources
        val channelId = getString(R.string.default_notification_channel_id)

        // Build a Notification object
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(notification.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android 8.0+ (API level 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "Firebase Messages"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        // Display the notification
        notificationManager.notify(notificationTag, notificationId, notificationBuilder.build())
    }
}