package com.example.share5

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.share5.ui.theme.hexColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ShowWifiDevices(navController: NavController, activity: MainActivity) {
    val hexColor = 0xFF1F3D
    val buttonColor = Color(hexColor)
    var showCard by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier
        .padding(0.dp)
        .background(Color.Black),
        bottomBar = {
            BottomAppBar(elevation = 0.dp,
                backgroundColor = buttonColor,
                contentColor = Color.White,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        IconButton(onClick = {
                            navController.navigateUp()
                        }) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                        IconButton(onClick = {
                            navController.navigate(Screens.Settings.route)
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        },
        content = {
            it
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.svg_water_wave_animation),
                    contentDescription = "design",
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 516.dp)
                        .size(400.dp)
                        .rotate(180f),
                    //.align(Alignment.BottomCenter)
                )
            }
        })
    Box() {
        val hexColor = 0xFF1F3D
        val buttonColor = Color(hexColor)

        LazyColumn {
            items(activity.Peers.size) { i ->
                var color = "#6f43fa".color
                var txtcol = Color.White

                if (activity.Peers[i].status == 0) {
                    color = "#49a078".color
                    //txtcol=Color.Red
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (activity.Peers[i].status == 3) {
                                connect(activity, activity.Peers[i])
                            } else {
                                disconnect(activity)
                            }
                        }
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                        .size(height = 50.dp, width = 150.dp),
                    backgroundColor = color,
                    elevation = 15.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(activity.Peers[i].deviceName, color = txtcol)
                    }
                }

            }
        }
    }
var buttonState by remember { mutableStateOf(false) }
val coroutineScope = rememberCoroutineScope()
Box {
}
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Bottom,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Row(
        modifier = Modifier
            .padding(bottom = 155.dp),
        // .align(Alignment.BottomCenter),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                buttonState = true
                coroutineScope.launch {
                    delay(3000) // Wait for 5 seconds
                    buttonState = false
                }
                activity.discovered = true;discoverPeers(activity)
            },
            modifier = Modifier
                .size(height = 37.dp, width = 125.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = (if (buttonState) Color.DarkGray else "#6f43fa".color),
                contentColor = Color.White
            )
        ) {
            Text(if (buttonState) "Searching..." else "Search")
        }
    }
}}



