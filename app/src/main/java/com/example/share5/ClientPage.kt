package com.example.share5

import android.R.id
import android.annotation.SuppressLint
import android.app.appsearch.SearchResult
import android.content.Context
import android.graphics.Color.parseColor
import android.graphics.Paint.Align
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons

import android.nfc.tech.IsoDep.get
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ClientPage(navHostController: NavHostController,activity: MainActivity){

    CoroutineScope(Dispatchers.IO).launch {
        Client(activity,device = activity.host,activity)
    }


    Surface(Modifier.fillMaxSize(1f)) {
        val hexColor = 0xFF1F3D
        val buttonColor = Color(hexColor)


        Scaffold(modifier = Modifier.padding(0.dp),
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,

                ) {
                    Column(verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        CustomComponent(activity,indicatorValue = activity.cnet)
                        LazyColumn(){
                           items(activity.numFiles){
                               Log.d("App","items = $it")
                                     cards()
                           }

                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.svg_water_wave_animation),
                        contentDescription = "design",
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 516.dp)
                            .size(400.dp)
                            .rotate(180f),
                    )
                }
            })
    }


}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = "#6f43fa".color,
            strokeWidth = 10.dp
        )
        Text(
            text = "Receiving",
            color = Color.DarkGray,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun cards(name:String = "no",size:Long = 0){
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(172, 134, 246),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()

    ){
        Text(name)

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun Show(){
    //cards()
}

