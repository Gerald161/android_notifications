package com.example.notificationapp.Navigation

sealed class Screen (val route: String) {
    object HomeScreen : Screen("home")
    object DetailScreen : Screen("detail_screen/{message}"){
        fun passValue(message: String) : String{
            return "detail_screen/${message}"
        }
    }
}