package com.example.share5

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Send(navController: NavController,activity: MainActivity){



    var selectedFiles by rememberSaveable{ mutableStateOf(emptyList<Uri>()) }
    var fileNameList  by rememberSaveable{ mutableStateOf(emptySet<String>()) }
    Surface(Modifier.fillMaxSize(1f)) {
        val hexColor = 0xFF1F3D
        val buttonColor = Color(hexColor)

        Box() {
            Scaffold(modifier = Modifier
                .padding(0.dp)
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
                    if (selectedFiles != emptyList<Uri>()) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Button(modifier = Modifier
                                .offset(y = 600.dp)
                                .align(Alignment.BottomCenter),
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

                })
            Column() {


                App(selectedFiles, add = {
                    selectedFiles += it
                    val temp = selectedFiles.toSet()
                    selectedFiles = temp.toList()


                }, minus = {
                    selectedFiles -= it
                })

                for (uri in selectedFiles) {
                    activity.filesName += getFileName(uri = uri)
                }


            }
        }
}
}


