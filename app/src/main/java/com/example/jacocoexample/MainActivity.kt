package com.example.jacocoexample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.jacocoexample.ui.main.MainFragment
import com.example.jacocoexample.ui.main.OverlayDetectionLayout

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
//        check()

        val overlayLayout: OverlayDetectionLayout = findViewById(R.id.overlayLayout)
        overlayLayout.onOverlayDetected = {
            result.text = "found overlay app!\n $it"
        }
        overlayLayout.onNoOverlayDetected = {
            result.text = "not found\n $it"
        }

        val button = findViewById<Button>(R.id.goSetting)
        button.setOnClickListener {
            val intent = Intent()
            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

    }

    private fun check() {
        val buttonCheckTouch = findViewById<Button>(R.id.buttonCheckTouch)
        buttonCheckTouch.setOnTouchListener { _, event ->
            checkFlagObscure(event)
        }
    }

    private fun checkFlagObscure(event: MotionEvent) = event.let {
        val obscured = (it.flags and MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0
        val partially = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (it.flags and MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED) != 0
        } else {
            obscured
        }
        if (obscured || partially) {
            result.text = "obscured $obscured \n partially $partially \n\n found overlay app!"
            false
        } else {
            result.text = "obscured $obscured \n partially $partially \n\nnot found "
            true
        }
    }

    private fun setOnClickTestPref() {
//        val button = findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            movePref(applicationContext)
//            val number2 = newSharedPref.getInt("NUMBER", 0)
//            println("------------- newSharedPref $number2")
//        }
    }

    private fun movePref(context: Context) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        newSharedPref = EncryptedSharedPreferences.create(
            "encrypted_preference_file_name",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // use the shared preferences and editor as you normally would
        mEditor = newSharedPref.edit()
        mEditor.clear().commit()
        migrateToEncryptedSharedPreferences()
    }

    private fun migrateToEncryptedSharedPreferences() {
        val all = sharedPref.all
        if (all.isNotEmpty()) {
            copyTo(all)
        }
    }

    private fun copyTo(all: Map<String, *>) {
        val editor = newSharedPref.edit()

        for ((key, value) in all) {
            println("${key}=$value")
            if (value is Int) {
                editor.putInt(key, value).apply()

                val number2 = newSharedPref.getInt(key, 0)
                println("------------- copyTo $key : $number2")
            } else if(value is String) {
                editor.putString(key, value).apply()

                val number2 = newSharedPref.getString(key, "no value")
                println("------------- copyTo $key : $number2")
            }
        }
    }
}