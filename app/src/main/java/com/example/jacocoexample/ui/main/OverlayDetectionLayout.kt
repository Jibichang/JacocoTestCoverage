package com.example.jacocoexample.ui.main

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.FLAG_WINDOW_IS_OBSCURED
import android.view.MotionEvent.FLAG_WINDOW_IS_PARTIALLY_OBSCURED
import androidx.constraintlayout.widget.ConstraintLayout


class OverlayDetectionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    var onOverlayDetected: () -> Unit = {}
    var onNoOverlayDetected: () -> Unit = {}

    init {
        filterTouchesWhenObscured = true
    }

    override fun onFilterTouchEventForSecurity(event: MotionEvent?): Boolean {
        val flags = event?.flags ?: 0
        val partially = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            flags and FLAG_WINDOW_IS_PARTIALLY_OBSCURED != 0
        } else {
            false
        }
        val badTouch = partially || flags and FLAG_WINDOW_IS_OBSCURED != 0

        println("-----------isOverlay  $badTouch")
        return if(badTouch) {
            println("-----------isOverlay ")
            onOverlayDetected.invoke()
            false
        } else {
            println("-----------is not Overlay ")
            onNoOverlayDetected.invoke()
            super.onFilterTouchEventForSecurity(event)
        }
    }

}
