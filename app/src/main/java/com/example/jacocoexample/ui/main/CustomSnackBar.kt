package com.example.jacocoexample.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.jacocoexample.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.ContentViewCallback as ViewCallback

class CustomSnackBar(
    context: Context,
    parent: ViewGroup,
    content: View,
    contentViewCallback: ViewCallback
) : BaseTransientBottomBar<CustomSnackBar>(context, parent, content, contentViewCallback) {


    fun make(parent: ViewGroup, @Duration duration: Int): CustomSnackBar {
        val inflater = LayoutInflater.from(parent.context)
        val content: View = inflater.inflate(R.layout.toast_with_image, parent, false)
        val viewCallback: ViewCallback = object : ViewCallback {
            override fun animateContentIn(delay: Int, duration: Int) {
               // TODO "Not yet implemented"
            }

            override fun animateContentOut(delay: Int, duration: Int) {
               // TODO "Not yet implemented"
            }

        }
        val customSnackBar = CustomSnackBar(parent.context, parent, content, viewCallback)
        customSnackBar.view.setPadding(0, 0, 0, 0)
        customSnackBar.duration = duration
        return customSnackBar
    }


    fun CustomSnackBar.setText(text: CharSequence?): CustomSnackBar {
        val textView = view.findViewById<View>(R.id.snackbar_text) as TextView
        textView.text = text
        return this
    }
}