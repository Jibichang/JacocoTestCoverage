package com.example.jacocoexample.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

fun Context.checkVpn() {
    val connectivity: ConnectivityManager? = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    var hasVPNFound = false
    hasVPNFound = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networks = connectivity?.getNetworkCapabilities(connectivity.activeNetwork)
        networks?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
    } else {
        // get all network for < Android 7
        val networks = connectivity?.allNetworks ?: emptyArray()
        networks.any { network ->
            connectivity?.getNetworkCapabilities(network)?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
        }
    }
    Toast.makeText(this, "ข้อความ hasVPNFound $hasVPNFound", Toast.LENGTH_LONG).show()
}

val networkCallback = object : ConnectivityManager.NetworkCallback() {
    // network is available for use
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
    }

    // Network capabilities have changed for the network
    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        val vpn = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

        println("checkVpn onCapabilitiesChanged - vpn $vpn")
    }

    // lost network connection
    override fun onLost(network: Network) {
        super.onLost(network)
    }
}

fun Context.checkVpnCallback() {
    val networkRequest = NetworkRequest.Builder()
        .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
        .build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val connectivityManager= getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}

fun checkProxyVPN(context: Context) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networks: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val hasVPNFound = networks?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
        val proxyAddress = System.getProperty("http.proxyHost") ?: ""
        val proxyPort = (System.getProperty("http.proxyPort") ?: "-1").toInt()
        val hasProxyFound = proxyAddress.isNotEmpty() || proxyPort > 0
        // $proxyAddress:$proxyPort
        println("checkVpn checkProxyVPN - hasVPNFound $hasVPNFound")
        println("checkVpn checkProxyVPN - hasProxyFound $hasProxyFound")

    }
}

fun Context.checkVpnForAndroid5(callback: (String) -> Unit) {
    val connectivityManager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE)
            as ConnectivityManager

    var result = ""
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        val networks = connectivityManager.allNetworks

        for (network in networks) {
            /// 5. getNetworkInfo TYPE_VPN
            val networkInfo = connectivityManager.getNetworkInfo(network)
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_VPN) {
                result += "5. "
            }

            /// 2. getNetworkCapabilities hasTransport TRANSPORT_VPN
            val caps = connectivityManager.getNetworkCapabilities(network)
            val detected = caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: false
            if (detected) {
                result += "2. "
            }
        }

        /// 4. allNetworkInfo typeName
        for (allNetworkInfo in connectivityManager.allNetworkInfo) {
            if (allNetworkInfo.typeName.lowercase(Locale.getDefault()).contains("vpn") && allNetworkInfo.isConnected) {
                result += "4. "
            }
        }
        result = if (result.isNotEmpty()){
            "VPN not detected"
        } else {
            "VPN detected"
        }

        callback(result)
    }
}