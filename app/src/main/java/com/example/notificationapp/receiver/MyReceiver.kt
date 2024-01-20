package com.example.notificationapp.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import com.example.notificationapp.di.RESULT_KEY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MyReceiver: BroadcastReceiver() {
    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Named("reply_notification_builder")
    @Inject
    lateinit var replyNotificationBuilder: NotificationCompat.Builder


    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("MESSAGE")

        if(message != null){
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }else{
            val remoteInput = RemoteInput.getResultsFromIntent(intent!!)

            if(remoteInput != null){
                val input = remoteInput.getCharSequence(RESULT_KEY).toString()

                val person = Person.Builder().setName("Me").build()

                val message = NotificationCompat.MessagingStyle.Message(
                    input, System.currentTimeMillis(), person
                )

                val notificationStyle = NotificationCompat.MessagingStyle(person).addMessage(message)

                if(context?.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                    notificationManager.notify(
                        3,
                        replyNotificationBuilder
                            .setStyle(notificationStyle)
                            .build()
                    )
                }
            }
        }
    }

}