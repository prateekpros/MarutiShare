package com.example.share5


import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Send(navController: NavController,activity: MainActivity){

    if(!activity.grOwner)
    {
        Log.d("App","------------growner request sent -------")
        activity.info?.isGroupOwner  = true
    }


    var selectedFiles by rememberSaveable{ mutableStateOf(emptyList<Uri>()) }
   // var fileNameList  by rememberSaveable{ mutableStateOf(emptySet<String>()) }
    Surface(Modifier.fillMaxSize(1f)) {
        val hexColor = 0xFF1F3D
        val buttonColor = Color(hexColor)

        Box(modifier = Modifier.fillMaxSize(1f)) {
            Scaffold(modifier = Modifier//.fillMaxSize(.7f)
                .align(Alignment.BottomCenter),
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
                    Image(
                        painter = painterResource(id = R.drawable.svg_water_wave_animation),
                        contentDescription = "design",
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 516.dp)
                            .size(400.dp)
                            .rotate(180f),
                    )
                })
            Column {


                App(selectedFiles, add = {
                    selectedFiles = selectedFiles + it
                    val temp = selectedFiles.toSet()
                    selectedFiles = temp.toList()


                }, minus = {
                    selectedFiles = selectedFiles - it
                })


                for (uri in selectedFiles) {
                    activity.filesName += getFileName(uri = uri)
                }


            }

            if (selectedFiles != emptyList<Uri>()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                Button(modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 25.dp, top = 10.dp)
                    .offset(y = 400.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = "#6f43fa".color,
                        contentColor = Color.White
                    ),
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            Server(activity, selectedFiles,activity.filesName, activity = activity)
                        }
                    }) {
                    Text("Send it ", color = Color.White)
                }
                }

            }
        }
}

}


