package com.example.jacocoexample

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.jacocoexample.ui.main.OverlayDetectionLayout
import com.example.jacocoexample.ui.main.ScanActivity
import com.example.jacocoexample.ui.main.customSnackBar
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {

    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var newSharedPref: SharedPreferences

    lateinit var result: TextView
    lateinit var buttonCheckTouch: Button
    lateinit var buttonSetting: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, MainFragment.newInstance())
//                .commitNow()
//        }

        result = findViewById(R.id.textResult)
        buttonCheckTouch = findViewById(R.id.buttonCheckTouch)
        buttonSetting = findViewById(R.id.goSetting)

        result.text = HtmlCompat.fromHtml(
            getString(R.string.scammer_detect_usb_detail),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        setOverlay()
        buttonSetting.setOnClickListener {
            // toast image top
//            showToastWithImage("ข้อความ Toast",  getDrawable(R.drawable.ic_check_white))
//            Toast.makeText(this, "ข้อความ Toast", Toast.LENGTH_LONG).show()
            //green toast
//            showCustomNotificationOld(
//                activity = this,
//                message = "ข้อความ Toast",
//                marginTop = 16
//            )
        }
        buttonCheckTouch.setOnClickListener {
            // toast image top
//            showToastWithImage("ข้อความ Snackbar", null)
            customSnackBar("ข้อความ Snackbar", getDrawable(R.drawable.ic_check_white))

            // toast image left
//            customSnackBarNormalToast("ข้อความ Snackbar")
//            customSnackBarNormalToastImage("ข้อความ Snackbar")
            //green toast
//            showCustomNotification(
//                activity = this,
//                message = "ข้อความ Snackbar",
//                marginTop = 16
//            )
        }
//        checkVpn()
//        checkVpnCallback()
//        setScanQR()
    }

    private fun setOverlay() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            window.setHideOverlayWindows(true)
//        }
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
}