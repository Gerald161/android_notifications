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
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
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

        notificationManager.createNotificationChannel(channel)

        return notificationManager
    }
}