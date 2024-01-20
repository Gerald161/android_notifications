package com.example.notificationapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("first_builder") private val notificationBuilder: NotificationCompat.Builder,
    @Named("second_builder") private val notificationBuilder2: NotificationCompat.Builder,
    @Named("reply_notification_builder") private val notificationBuilder3: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) : ViewModel() {
    fun showSimpleNotification(context: Context){
        if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(1, notificationBuilder.build())
        }
    }

    fun updateNotification(context: Context){
        if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(1, notificationBuilder.setContentTitle("Updated Notification").build())
        }
    }

    fun cancelNotification(context: Context){
        if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.cancel(1)
        }
    }

    fun showProgress(context: Context){
        if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            val max = 10
            var progress = 0
            viewModelScope.launch(Dispatchers.IO) {
                while(progress != max){
                    delay(1000)
                    progress += 1
                    notificationManager.notify(
                        2, notificationBuilder2
                            .setContentTitle("Downloading")
                            .setContentText("${progress}/${max}")
                            .setProgress(max, progress, false)
                            .build()
                    )
                }

                notificationManager.notify(
                    2, notificationBuilder
                        .setContentTitle("Completed")
                        .setContentText("")
                        .setContentIntent(null)
                        .clearActions()
                        .setProgress(0,0,false)
                        .build()
                )
            }
        }
    }

    fun showReplyNotification(context: Context){
        if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(3, notificationBuilder3.build())
        }
    }
}