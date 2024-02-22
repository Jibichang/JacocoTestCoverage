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

    var onOverlayDetected: (String) -> Unit = {}
    var onNoOverlayDetected: (String) -> Unit = {}

    init {
        filterTouchesWhenObscured = true
    }

    override fun onFilterTouchEventForSecurity(event: MotionEvent?): Boolean {
        return checkOverlay(event)
    }

    private fun checkOverlay(event: MotionEvent?): Boolean {
        var badTouch = false
        var partially = false
        var obscured = false
        event?.let {
            val flags = it.flags
            /* this is only true if the window that received
            this event is obstructed in areas other than the touched location */
            obscured = (flags and FLAG_WINDOW_IS_OBSCURED) != 0
            /* event is partly or wholly obscured by another visible window
            above it and the event did not directly pass through the obscured area */
            partially = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                (flags and FLAG_WINDOW_IS_PARTIALLY_OBSCURED) != 0
            } else {
                false
            }

            println("-----------onFilterTouchEventForSecurity partially $partially obscured $obscured")
            badTouch = partially || obscured
        }

        println("-----------badTouch = $badTouch")
        return if (badTouch) {

            onOverlayDetected.invoke("partially = $partially \n obscured = $obscured \n overlay = $overlay")
            false
        } else {
            onNoOverlayDetected.invoke("is not Overlay, \nevent null? ${event == null}\n partially = $partially \n obscured = $obscured \n overlay = $overlay")
            super.onFilterTouchEventForSecurity(event)
        }
    }
}
