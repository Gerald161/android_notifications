package com.example.notificationapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.notificationapp.Navigation.Screen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(navController: NavHostController){
    val mainViewModel = hiltViewModel<MainViewModel>()

    val context = LocalContext.current

    var openDialog by remember { mutableStateOf(false) }

    val postNotificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { status ->
            if(!status){
                if(ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.POST_NOTIFICATIONS)){
                    openDialog = true
                }else{
                    Toast.makeText(
                        context,
                        "Please allow notification permission",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))

                    context.startActivity(intent)
                }
            }
        }
    )

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false

                Toast.makeText(
                    context,
                    "Please enable 'Notification' Permission in settings before you may proceed",
                    Toast.LENGTH_LONG
                ).show()

                //open app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))

                context.startActivity(intent)
            },
            title = {
                Text(text = "Permission needed")
            },
            text = {
                Text("Permission needed before you can video call")
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog = false

                        postNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }) {
                    Text("Ok")
                }
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(onClick = {
            if(context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED){
                postNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                mainViewModel.showSimpleNotification(context)
            }

        }) {
            Text(text = "Simple Notification")
        }

        Button(onClick = {
            mainViewModel.updateNotification(context)
        }) {
            Text("Update Notification")
        }

        Button(onClick = {
            navController.navigate(Screen.DetailScreen.passValue("Coming from Home Screen"))
        }) {
            Text("Details Screen")
        }

        Button(onClick = {
            mainViewModel.showProgress(context)
        }) {
            Text("Progress Notification")
        }

        Button(onClick = {
            mainViewModel.showReplyNotification(context)
        }) {
            Text("Reply Notification")
        }

        Button(onClick = {
            mainViewModel.cancelNotification(context)
        }) {
            Text("Cancel Notification")
        }
    }
}