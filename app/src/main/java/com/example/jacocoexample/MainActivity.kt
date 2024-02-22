package com.example.jacocoexample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jacocoexample.ui.main.OverlayDetectionLayout
import com.example.jacocoexample.ui.main.ScanActivity
import com.google.android.libraries.places.api.Places
import java.util.*

class MainActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            getString(R.string.pref_main_name), Context.MODE_PRIVATE)
    }
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var newSharedPref: SharedPreferences

    lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance())
//                .commitNow()
//        }

        result = findViewById(R.id.textResult)
//        setOverlay()
        checkVpnCallback()

//        setScanQR()
    }

    override fun onResume() {
        super.onResume()
        checkVpnForAndroid5 { text ->
            result.text = text
        }
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
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

    private fun Context.checkVpnCallback() {
        val networkRequest = NetworkRequest.Builder()
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager= getSystemService(ConnectivityManager::class.java) as ConnectivityManager
            connectivityManager.requestNetwork(networkRequest, networkCallback)
        }
    }


    private fun Context.checkVpnForAndroid5(callback: (String) -> Unit) {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE)
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

    private fun setScanQR() {
        val buttonScan = findViewById<Button>(R.id.buttonOpenScan)
        buttonScan.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        println("----------------- apiKey $apiKey")
        // Initialize the SDK
        Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)
    }

    private fun setOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setHideOverlayWindows(true)
        }
        val overlayLayout: OverlayDetectionLayout = findViewById(R.id.overlayLayout)
        overlayLayout.onOverlayDetected = {
            result.text = "found overlay app!\n $it"
        }
        overlayLayout.onNoOverlayDetected = {
            result.text = "not found\n $it"
        }


//        val button = findViewById<Button>(R.id.goSetting)
//        button.setOnClickListener {
//            val intent = Intent()
//            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            val uri = Uri.fromParts("package", packageName, null)
//            intent.data = uri
//            startActivity(intent)
//        }
    }

//    private fun movePref(context: Context) {
//        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//
//        newSharedPref = EncryptedSharedPreferences.create(
//            "encrypted_preference_file_name",
//            masterKeyAlias,
//            context,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//
//        // use the shared preferences and editor as you normally would
//        mEditor = newSharedPref.edit()
//        mEditor.clear().commit()
//        migrateToEncryptedSharedPreferences()
//    }

//    private fun migrateToEncryptedSharedPreferences() {
//        val all = sharedPref.all
//        if (all.isNotEmpty()) {
//            copyTo(all)
//        }
//    }

//    private fun copyTo(all: Map<String, *>) {
//        val editor = newSharedPref.edit()
//
//        for ((key, value) in all) {
//            println("${key}=$value")
//            if (value is Int) {
//                editor.putInt(key, value).apply()
//
//                val number2 = newSharedPref.getInt(key, 0)
//                println("------------- copyTo $key : $number2")
//            } else if(value is String) {
//                editor.putString(key, value).apply()
//
//                val number2 = newSharedPref.getString(key, "no value")
//                println("------------- copyTo $key : $number2")
//            }
//        }
//    }
}