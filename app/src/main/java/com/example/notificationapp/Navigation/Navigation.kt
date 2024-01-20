package com.example.notificationapp.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.notificationapp.HomeScreen
import com.example.notificationapp.Navigation.Screens.DetailScreen

const val DEEP_LINK_URI = "https://kofi.com"

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController)
        }
        composable(
            route = Screen.DetailScreen.route,
            arguments = listOf(navArgument("message"){
                type = NavType.StringType
            }),
            deepLinks = listOf(navDeepLink { uriPattern = "$DEEP_LINK_URI/message={message}" })
        ){
            DetailScreen(message = it.arguments?.getString("message").toString())
        }
    }
}