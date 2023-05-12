package com.example.share5

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo

import android.net.wifi.p2p.WifiP2pManager

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.share5.ui.theme.Share3Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File

import java.net.*
import java.util.*

class MainActivity : ComponentActivity() {

    lateinit var manager: WifiP2pManager
    lateinit var channel: WifiP2pManager.Channel
    private lateinit var receiver:WiFiDirectBroadcastReceiver
    val peers = mutableStateListOf<WifiP2pDevice>()
    lateinit var  peerListListener : WifiP2pManager.PeerListListener
    var Peers:List<WifiP2pDevice> = peers
    var host: InetAddress = InetAddress.getByName("0.0.0.0")
    var msg :String = ""
    var deviceConnected:String by mutableStateOf("")
    var connected:Boolean = false
    lateinit var navController: NavHostController
    var filesName = emptyList<String>()
    var discovered by mutableStateOf(false)
    var cnet by mutableStateOf(0f)
    var numFiles = 0
    val port:Int = 36912
    var grOwner:Boolean = false
    var info:WifiP2pInfo? = null


    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }


    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContent {
            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(
                key1 = lifecycleOwner, effect = {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                })

            Share3Theme {
                navController= rememberNavController()
                SetupNavGraph(navController,this)


            }

        }

        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this , mainLooper,null)
        discoverPeers(this)


    }



    override fun onResume() {
        super.onResume()
        receiver = WiFiDirectBroadcastReceiver(manager,channel,this)
        registerReceiver(receiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }


    fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it,"Share5Media").apply { mkdirs() } }
        return if ((mediaDir != null) && mediaDir.exists()) mediaDir else filesDir
    }

}

@SuppressLint("MissingPermission")
fun discoverPeers(activity: MainActivity) {
    refresh(activity = activity)
    activity.manager.discoverPeers(activity.channel,object : WifiP2pManager.ActionListener{
        override fun onSuccess() {
            Log.d("App","Success")
            activity.peers.forEach{
                Log.d("App","devices = ${it.deviceName}")
            }
            activity.discovered = false
        }

        override fun onFailure(p0: Int) {
            Log.d("App","Failure")
        }

    })

}


fun refresh(activity: MainActivity){
    activity.peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList
        if (refreshedPeers != activity.peers) {
            activity.peers.clear()
            activity.peers.addAll(refreshedPeers)
        }

        if (activity.peers.isEmpty()) {
            Log.i("App", "No devices found")
            return@PeerListListener
        }
    }
}


@SuppressLint("MissingPermission")
fun connect(activity: MainActivity, device: WifiP2pDevice) {

    Log.i("App", "Connection request sent to ${device.deviceName}")

    val config = WifiP2pConfig().apply {
//        groupOwnerIntent = -2
        deviceAddress = device.deviceAddress
        wps.setup = WpsInfo.PBC

    }


    Log.d("App","device intent value = ${config.groupOwnerIntent}")

    activity.manager.connect(activity.channel, config, object : WifiP2pManager.ActionListener {

        override fun onSuccess() {
           Log.d("App","-------------connected ------------")

                }

        override fun onFailure(reason: Int) {

            Log.i("App", "Could not connect")

        }
    })
}

@SuppressLint("MissingPermission")
fun disconnect(activity: MainActivity){
    activity.manager.requestGroupInfo(activity.channel) { group ->

        activity.manager.removeGroup(activity.channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d("App","Disconnected Successful")
            }

            override fun onFailure(reason: Int) {
                Log.d("App","Disconnected UnSuccessful")
            }
        })
    }
}

fun changeGroupOwner(activity: MainActivity) {
    // Disband the existing group
    activity.manager.removeGroup(activity.channel, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            // Group removal successful, initiate group formation
            createGroup(activity)
        }

        override fun onFailure(reason: Int) {
            // Group removal failed, handle the failure scenario
            Log.e("App", "Failed to remove group. Reason: $reason")
        }
    })
}


private fun createGroup(activity: MainActivity) {
    if (ActivityCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.NEARBY_WIFI_DEVICES
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        activity.manager.createGroup(activity.channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // Group creation successful
                Log.d("App", "New group created")
            }

            override fun onFailure(reason: Int) {
                // Group creation failed, handle the failure scenario
                Log.e("App", "Failed to create group. Reason: $reason")
            }
        })
        return
    }

}











