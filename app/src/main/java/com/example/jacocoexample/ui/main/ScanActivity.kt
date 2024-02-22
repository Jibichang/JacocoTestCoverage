package com.example.jacocoexample.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jacocoexample.R
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class ScanActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fullscreenContent = findViewById<TextView>(R.id.fullscreen_content)


        fullscreenContent.setOnClickListener {
            val options = GmsBarcodeScannerOptions.Builder()
                .enableAutoZoom()
                .build()

            val scanner: GmsBarcodeScanner = GmsBarcodeScanning.getClient(this, options)

            scanner.startScan()
                .addOnSuccessListener { barcodes ->
                    barcodes.rawValue?.let {
                        println("------------- Result : $it")
//                        fullscreenContent.text = "Result : $it"
                    }
                }
                .addOnFailureListener {
                    println("------------- Result : ${it.message}")
//                    fullscreenContent.text = "Result : ${it.message}"
                }
        }

    }

}