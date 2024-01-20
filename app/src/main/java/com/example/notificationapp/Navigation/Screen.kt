package com.example.notificationapp.Navigation

sealed class Screen (val route: String) {
    object HomeScreen : Screen("home")
}