package com.example.share5

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

//
//@Composable
//fun HomeScreen(navHostController: NavHostController){
//
//    Surface(Modifier.fillMaxSize(1f)) {
//
//        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
//
//            Row() {
//                Button(onClick = { navHostController.navigate(Screens.Send.route) }) {
//                    Text("Send Files")
//                }
//                Spacer(modifier = Modifier.padding(start = 50.dp))
//
//                Button(onClick = { navHostController.navigate(Screens.Receive.route)}) { Text("Receive Files")
//                }
//            }
//            Spacer(modifier = Modifier.padding(bottom =200.dp))
//
//        }
//    }
//}