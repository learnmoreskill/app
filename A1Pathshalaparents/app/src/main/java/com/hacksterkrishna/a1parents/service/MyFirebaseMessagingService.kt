package com.hacksterkrishna.a1parents.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.activity.SplashActivity




/**
 * Created by hacksterkrishna on 18/12/17.
 */

class MyFirebaseMessagingService : FirebaseMessagingService(){

    private var pref: SharedPreferences? = null
    private val NOTIFICATION_ID = 1
    private val NOTIFICATION_CHANNEL_ID = "misc"

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        pref=getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        if(pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
            showMessage(p0?.notification)
        }
    }

    private fun showMessage(notification: RemoteMessage.Notification?) {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { NotificationChannel(NOTIFICATION_CHANNEL_ID, "Miscellaneous", NotificationManager.IMPORTANCE_DEFAULT) } else { null }

            // Configure the notification channel.
            notificationChannel?.description = "Channel for miscellaneous notifications"
            notificationChannel?.enableLights(true)
            notificationChannel?.lightColor = Color.RED
            notificationChannel?.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intent = Intent(this,SplashActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        val notifBuilder=NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setTicker(notification?.title)
                .setSmallIcon(R.drawable.ic_notif_icon)
                .setContentTitle(notification?.title)
                .setContentText(notification?.body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID,notifBuilder.build())

    }

}