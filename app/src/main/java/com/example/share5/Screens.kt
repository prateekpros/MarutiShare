package com.example.share5

sealed class Screens(val route: String){
    object HomeScreen: Screens("Home")
    object Send: Screens("Send")
    object Receive: Screens("Receive")
    object ClientPage: Screens("Client")
    object First: Screens("First")
    object fileSelect: Screens("File Select")
    object Settings: Screens("Settings")
    object Profile: Screens("Profile")

}