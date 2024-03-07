package com.example.jacocoexample.ui.main

import android.content.Context
import android.content.SharedPreferences
import com.example.jacocoexample.R


//private fun movePref(context: Context) {
//    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//
//    newSharedPref = EncryptedSharedPreferences.create(
//        "encrypted_preference_file_name",
//        masterKeyAlias,
//        context,
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )
//
//    // use the shared preferences and editor as you normally would
//    mEditor = newSharedPref.edit()
//    mEditor.clear().commit()
//    migrateToEncryptedSharedPreferences()
//}

//val sharedPref: SharedPreferences by lazy {
//    applicationContext.getSharedPreferences(
//        getString(R.string.pref_main_name), Context.MODE_PRIVATE)
//}

//private fun migrateToEncryptedSharedPreferences() {
//    val all = sharedPref.all
//    if (all.isNotEmpty()) {
//        copyTo(all)
//    }
//}

//private fun copyTo(all: Map<String, *>) {
//    val editor = newSharedPref.edit()
//
//    for ((key, value) in all) {
//        println("${key}=$value")
//        if (value is Int) {
//            editor.putInt(key, value).apply()
//
//            val number2 = newSharedPref.getInt(key, 0)
//            println("------------- copyTo $key : $number2")
//        } else if(value is String) {
//            editor.putString(key, value).apply()
//
//            val number2 = newSharedPref.getString(key, "no value")
//            println("------------- copyTo $key : $number2")
//        }
//    }
//}