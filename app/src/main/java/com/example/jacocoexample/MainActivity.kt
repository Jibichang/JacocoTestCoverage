package com.example.jacocoexample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.jacocoexample.ui.main.MainFragment


class MainActivity : AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            getString(R.string.pref_main_name), Context.MODE_PRIVATE)
    }
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var newSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

//        with (sharedPref.edit()) {
//            putInt("NUMBER", 1)
//            putString("STRING", "my application")
//            apply()
//        }

        val number = sharedPref.getInt("NUMBER", 0)
        println("------------- sharedPref $number")


        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            movePref(applicationContext)
            val number2 = newSharedPref.getInt("NUMBER", 0)
            println("------------- newSharedPref $number2")
        }
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