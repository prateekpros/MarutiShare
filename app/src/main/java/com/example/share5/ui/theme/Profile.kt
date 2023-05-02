package com.example.share5.ui.theme

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.share5.R
import com.example.share5.ui.theme.Share3Theme
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.share5.MainActivity
import com.example.share5.Screens

@Composable
fun Profile(navHostController: NavHostController,activity: MainActivity) {
    var name by remember { mutableStateOf("Shivam Shukla") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
//            Image(painter = painterResource(id = R.drawable._135768),
//                contentDescription = "My Photo",
//                modifier = Modifier.clickable {navHostController.navigate(Screens.View.route)})
            ProfileImg()
//            Image(
//                painter = rememberImagePainter(data = imageUrl, builder = {
//                    crossfade(true)
//                    placeholder(R.drawable._135768)
//                }),
//                contentDescription = "Profile Picture",
//                modifier = Modifier
//                    .size(150.dp)
//                    //.clip(CircleShape)
//                    .background(Color.Black)
////                    .border(2.dp, color = Color.Red)
//            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navHostController.navigateUp() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(Color.Black)
        ) {
            Text("Save", color = Color.White)
        }
    }
}

@Composable
fun ProfileImg() {
    var isFullScreen by remember { mutableStateOf(false) }

    val painter = painterResource(id = R.drawable._135768)

    if (isFullScreen) {
        // Show full-screen image
        openImg(image = painter) {
            isFullScreen = false
        }
    } else {
        // Show regular-sized image
        Image(
            painter = painter,
            contentDescription = "My Photo",
            modifier = Modifier.clickable(onClick = { isFullScreen = true })
        )
    }
}

@Composable
fun openImg(image: Painter, onClose: () -> Unit) {
    Dialog(onDismissRequest = onClose) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable(onClick = onClose),
                contentScale = ContentScale.Fit
            )
        }
    }
}



//fun changeDeviceName(activity: MainActivity,name:String) {
//
//     val wifiManager: WifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//    activity.manager.setDeviceName(activity.channel, name, object : WifiP2pManager.ActionListener {
//        override fun onSuccess() {
//            // Name changed successfully
//        }
//
//        override fun onFailure(reason: Int) {
//            // Name change failed
//        }
//    })
//}
