package com.example.share5.ui.theme

import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Settings() {
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Settings") }
//            )
//        }
    ) {it
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
//            Text(
//                text = "General",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
            SettingItem(
                icon = Icons.Default.BrightnessMedium,
                title = "Display",
                description = "Adjust screen brightness, sleep time, and more",
                onClick = { /* TODO */ }
            )
            SettingItem(
                icon = Icons.Default.NetworkWifi,
                title = "Network & Internet",
                description = "Wi-Fi, mobile data, hotspot, VPN, and airplane mode",
                onClick = { /* TODO */ }
            )
//            Text(
//                text = "System",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
            SettingItem(
                icon = Icons.Default.Storage,
                title = "Storage",
                description = "View and manage storage space on this device",
                onClick = { /* TODO */ }
            )
            SettingItem(
                icon = Icons.Default.Lock,
                title = "Security & location",
                description = "Screen lock, encryption, and device admin apps",
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 16.dp)
        )
        Column {
//            Text(
//                text = title,
//                fontWeight = FontWeight.SemiBold,
//                fontSize = 16.sp,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//            Text(
//                text = description,
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
        }
    }
}