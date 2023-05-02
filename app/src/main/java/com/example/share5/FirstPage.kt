package com.example.share5

//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.RowScopeInstance.align
//import androidx.compose.foundation.layout.RowScopeInstance.align
//import androidx.compose.foundation.layout.RowScopeInstance.align

import android.R.id
import android.content.Context
import android.graphics.Color.parseColor
import android.graphics.Paint.Align
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons

import android.nfc.tech.IsoDep.get
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun HomeScreen(navHostController: NavHostController,activity: MainActivity){
    //var selectedIndex by remember { mutableStateOf(0) }
    //var isTextVisible by remember { mutableStateOf(false) }
    var showCard by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize(1f)) {
      val hexColor = 0xFF1F3D
      val buttonColor = Color(hexColor)

    Scaffold(modifier = Modifier.padding(0.dp),
        topBar = {
            TopAppBar(elevation = 0.dp,
                title = {
                    Row(verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rockshare),
                            contentDescription = "Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Free Share")
                        Spacer(modifier = Modifier.width(150.dp))
                        Box(modifier = Modifier.clip(CircleShape)) {
                        Image(
                            painter = painterResource(id = R.drawable._135768),
                            contentDescription = "My Image",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { navHostController.navigate(Screens.Profile.route) }
                        ) }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(elevation=0.dp,
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
                            navHostController.navigateUp()
                        }) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                        IconButton(onClick = {
                            navHostController.navigate(Screens.Settings.route)
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                }
            )
        },
        content = { it
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.svg_water_wave_animation),
                    contentDescription = "design",
                    modifier = Modifier
                        .size(400.dp)
                        .offset(y = (-210).dp)
                )
                //val iconDrawable = ContextCompat.getDrawable(context, R.drawable.select1091223)
                Image(
                    painter = painterResource(id = R.drawable._0230421_141606_removebg_preview),
                    contentDescription = "design",
                    modifier = Modifier
                        .offset(y = (-430).dp)
                        .size(400.dp)
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .offset(y = 300.dp)
                            .clip(CircleShape)
                            .background("#6f43fa".color)
                            .clickable { navHostController.navigate(Screens.Send.route) },
                        contentAlignment = Alignment.TopCenter
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.select1091223),
                            contentDescription = "Select",
                            modifier = Modifier
                                .size(60.dp)
                                .offset(x = 2.dp, y = 2.dp)
                                .align(Alignment.Center)
                            //.clickable {  navHostController.navigate(Screens.Send.route)  }
                        )
                    }
                    Spacer(modifier = Modifier.padding(40.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .offset(y = 300.dp)
                            .clip(CircleShape)
                            .background("#6f43fa".color)
                            .clickable { navHostController.navigate(Screens.ClientPage.route) },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Image(
                            painter = painterResource(R.drawable._537278),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Receive",
                            modifier = Modifier
                                .size(60.dp)
                                .offset(x = 0.dp, y = 10.dp)
                            //.clickable { navHostController.navigate(Screens.Client.route) }
                        )
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.svg_water_wave_animation),
                    contentDescription = "design",
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 460.dp)
                        .size(400.dp)
                        .rotate(180f),
                    //.align(Alignment.BottomCenter)
                )

                   //------------------------------------


                if(showCard){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                disconnect(activity)
                            }
                            .padding(top = 7.dp, bottom = 0.dp, start = 8.dp, end = 7.dp)
                            .size(height = 20.dp, width = 150.dp),
                        backgroundColor = "#6f43fa".color,
                        elevation = 10.dp
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = activity.deviceConnected, color = Color.Black)
                        }
                    }}
                    //----------------------------
                    Button(modifier = Modifier
                        //.clip(RoundedCornerShape(100.dp))
                        //.background(color = "#FF1F3D".color)
                        //.background(color = Blue2)
                        .padding(10.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = 500.dp),
                        //.align(Alignment.Center),
                        //.align(Alignment.Center)
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = "#6f43fa".color,
                            contentColor = Color.White
                        ),
                        onClick = {
                            navHostController.navigate(Screens.Receive.route)
                        }) {
                        Text(text = "P2P Devices")
                    }

                }
            })
    }
}

val String.color
    get() = Color(parseColor(this))





//fun onClick1(navHostController: NavHostController) {
//    Screens.Send.route
//}
//
//fun onClick2(navHostController: NavHostController) {
//    Screens.Receive.route
//}

//Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .offset(y = 310.dp, x = -100.dp),
//                            //.padding(10.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        IconButton(
//                            onClick = { navHostController.navigate(Screens.Send.route) },
//                            modifier = Modifier
//                                .padding(10.dp)
//                                .size(20.dp)
//                                .background(color = "#6f43fa".color)
//                        )
//                        {
//                            Icon(
//                                painter = painterResource(id = R.drawable._537278),
//                                contentDescription = "Add",
//                                tint = Color.White
//                            )
//                        }
//
//                        Button(modifier = Modifier
//                            //.clip(RoundedCornerShape(100.dp))
//                            //.background(color = "#FF1F3D".color)
//                            //.background(color = Blue2)
//                            .padding(10.dp)
//                            .offset(y = 250.dp),
//                            //.align(Alignment.Center),
//                            shape= CircleShape,
//                            colors = ButtonDefaults.buttonColors(
//                                backgroundColor = "#6f43fa".color,
//                                contentColor = Color.White
//                            ),
//                            onClick = {navHostController.navigate(Screens.Receive.route)
//                            }) {
//                            Text(text = "Send")
//                        }
//                        Button(modifier = Modifier
//                            //.clip(RoundedCornerShape(100.dp))
//                            //.background(color = "#FF1F3D".color)
//                            //.background(color = Blue2)
//                            .padding(10.dp)
//                            .offset(y = 250.dp),
//                            //.align(Alignment.Center),
//                            shape= CircleShape,
//                            colors = ButtonDefaults.buttonColors(
//                                backgroundColor = "#6f43fa".color,
//                                contentColor = Color.White
//                            ),
//                            onClick = {navHostController.navigate(Screens.Receive.route)
//                            }) {
//                            Text(text = "Receive")
//                        }


//}


//Image(
//                        painter = painterResource(id = R.drawable.svg_water_wave_animation),
//                        contentDescription = "design",
//                        modifier = Modifier
//                            .size(400.dp)
//                            .rotate(180f)
//                    )
//                    Row() {
//                        Button(onClick = { navHostController.navigate(Screens.Send.route) }) {
//                            Text("Send Files")
//                        }
//                        Spacer(modifier = Modifier.padding(start = 50.dp))
//
//                        Button(onClick = { navHostController.navigate(Screens.Receive.route) }) {
//                            Text("Receive Files")
//                        }
//                    }
//                    Spacer(modifier = Modifier.padding(bottom = 200.dp))

//IconButton(onClick = {navHostController.navigate(Screens.Send.route)},
//modifier = Modifier
//.offset(y = 350.dp)
//.background("#6f43fa".color)) {
//    Icon(
//        imageVector = Icons.Filled.Save,
//        contentDescription = "Select",
//        tint = Color.Red,
//        modifier = Modifier.size(24.dp)
//    )
//}
//Spacer(modifier = Modifier.padding(50.dp).clip(CircleShape))
//IconButton(onClick = {navHostController.navigate(Screens.Receive.route)},
//modifier = Modifier
//.offset(y = 350.dp)
//.background("#6f43fa".color)) {
//    //val painter = painterResource(R.drawable.select1091223)
//    Icon(
//        imageVector = Icons.Filled.Receipt,
//        contentDescription = "Receive",
//        tint = Color.Red,
//        modifier = Modifier.size(24.dp)
//    )
//}
