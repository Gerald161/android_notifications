package com.example.notificationapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.example.notificationapp.MainActivity
import com.example.notificationapp.Navigation.DEEP_LINK_URI
import com.example.notificationapp.R
import com.example.notificationapp.receiver.MyReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

const val RESULT_KEY = "RESULT_KEY"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    @Named("first_builder")
    fun provideNotificationBuilder(
      @ApplicationContext context: Context
    ) : NotificationCompat.Builder{
        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("MESSAGE", "Hello Kofi")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "$DEEP_LINK_URI/message=Coming from Notification".toUri(),
            context,
            MainActivity::class.java
        )

        val clickPendingIntent = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(context, "Main Channel ID")
            .setContentTitle("Welcome")
            .setContentText("Gerald Darko")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .addAction(0,"ACTION", pendingIntent)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
    }

    @Singleton
    @Provides
    @Named("second_builder")
    fun provideSecondNotificationBuilder(
        @ApplicationContext context: Context
    ) : NotificationCompat.Builder{
        return NotificationCompat.Builder(context, "Second Channel ID")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setOngoing(true)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Singleton
    @Provides
    @Named("reply_notification_builder")
    fun provideReplyNotificationBuilder(
        @ApplicationContext context: Context
    ) : NotificationCompat.Builder{
        val remoteInput = RemoteInput.Builder(RESULT_KEY)
            .setLabel("Type here...")
            .build()

        val replyIntent = Intent(context, MyReceiver::class.java)

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            replyIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val person = Person.Builder().setName("Kofi").build()

        val notificationStyle = NotificationCompat.MessagingStyle(person)
            .addMessage("Hello there", System.currentTimeMillis(), person)

        val replyAction = NotificationCompat.Action.Builder(
            0,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        return NotificationCompat.Builder(context, "Main Channel ID")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setOnlyAlertOnce(true)
            .setStyle(notificationStyle)
            .addAction(replyAction)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat{
        val notificationManager  = NotificationManagerCompat.from(context)

        val channel = NotificationChannel(
            "Main Channel ID",
            "Main Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val channel2 = NotificationChannel(
            "Second Channel ID",
            "Second Channel",
            NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(channel2)

        return notificationManager
    }
}