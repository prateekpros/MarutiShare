package com.example.share5

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


@SuppressLint("ServiceCast")
class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: MainActivity
) : BroadcastReceiver() {

    private var wifiManager: WifiManager =
        activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                when (intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        Log.d("App", "Wifi P2P is Enabled ")
                    }
                    else -> {
                        Log.d("App", "Wifi P2P is Not Enabled, Enabling it ... ")
                        wifiManager.isWifiEnabled = true
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                manager.requestPeers(channel, activity.peerListListener)

            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                manager.requestConnectionInfo(channel
                ) { info ->
                    if (info != null) {
                        activity.info = info
                        if (info.groupOwnerAddress != null) {
                            activity.host = info.groupOwnerAddress
                            Log.d("App","group Owner  address :${info.groupOwnerAddress}")
                            Log.d("App","group owner or not  ${info.isGroupOwner}")
                            activity.grOwner = info.isGroupOwner
                        } else {
                            Log.e("App", "Connection info is null")
                        }
                    } else {
                        Log.e("App", "Connection info is null")
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {

                val wifiP2pDevice = intent.getParcelableExtra<WifiP2pDevice>(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
                if (wifiP2pDevice != null) {
                    // Handle the changes in the Wi-Fi Direct device details
                    // You can access different properties of wifiP2pDevice such as device name, address, etc.
                    Log.d("App", "Device name: ${wifiP2pDevice.deviceName}")
                    Log.d("App", "Device address: ${wifiP2pDevice.deviceName}")
                    // Handle other properties or perform actions based on the device details
                }

            }
        }

    }
}