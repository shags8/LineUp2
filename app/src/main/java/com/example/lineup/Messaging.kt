package com.example.lineup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.gdsc.lineup2024.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId="notification_channel"
const val channelName="com.example.lineup"
class Messaging : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
    private fun getRemoteView(title: String, description: String): RemoteViews? {
        val remoteView=RemoteViews("com.gdsc.lineup2024", R.layout.notification)
        remoteView.setTextViewText(R.id.notiTitle,title)
        remoteView.setTextViewText(R.id.notiMessage,description)
        remoteView.setImageViewResource(R.id.notiImg,R.drawable.blue_avatar)
        return remoteView

    }

    fun generateNotification(title:String,description:String){
        val intent= Intent(this,WelcomePage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var builder: NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,
            channelId).setSmallIcon(R.drawable.lineup).setAutoCancel(true).setVibrate(
            longArrayOf(1000,1000,1000,1000)).setOnlyAlertOnce(true).setContentIntent(pendingIntent)

        builder.setContent(getRemoteView(title, description))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel= NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
}
