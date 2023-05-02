package com.example.share5

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.share5.ui.theme.Profile
import com.example.share5.ui.theme.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SetupNavGraph(navController: NavHostController,activity: MainActivity){

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route ){
        composable(
            route = Screens.HomeScreen.route
        ){
            HomeScreen(navController,activity)
        }

//        composable(
//            route = Screens.fileSelect.route
//        ){
//            App(navController,activity)
//        }

        composable(
            route = Screens.ClientPage.route
        ){
            ClientPage(navController, activity )
        }

        composable(
            route = Screens.Profile.route
        ){
            Profile(navController,activity)
        }

        composable(
            route = Screens.First.route
        ){
            Send(navController,activity)
        }

        composable(
            route = Screens.Settings.route
        ){
            Settings()
        }

        composable(
            route = Screens.Send.route
        ){
            Send(navController,activity)
        }

        composable(
            route = Screens.Receive.route
        ){
            ShowWifiDevices(navController,activity)
        }

    }
}