package com.example.jacocoexample.ui.main

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.example.jacocoexample.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun Activity.showToastWithImage(msg: CharSequence, img: Drawable?) {
    val toastView = layoutInflater.inflate(R.layout.toast_with_image, null)
    val tvToastSuccess = toastView.findViewById<TextView>(R.id.tvToastSuccess)
    tvToastSuccess.text = msg
    tvToastSuccess.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null)

    val toast = Toast(this)
    toast.view = toastView
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}

fun Activity.customSnackBar(msg: CharSequence, img: Drawable?) {
    val snackBar: Snackbar = Snackbar.make(this.window.decorView.findViewById(android.R.id.content),
        "", Snackbar.LENGTH_LONG)
    val customSnackView: View = layoutInflater.inflate(R.layout.toast_with_image, null)

    val tvToastSuccess = customSnackView.findViewById<TextView>(R.id.tvToastSuccess)
    tvToastSuccess.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null)
    tvToastSuccess.text = msg
    tvToastSuccess.gravity = Gravity.CENTER

    val layoutParams = snackBar.view.layoutParams as FrameLayout.LayoutParams
    layoutParams.gravity = Gravity.CENTER
    layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT
    snackBar.view.layoutParams = layoutParams
    snackBar.view.background = ContextCompat.getDrawable(this, R.drawable.bg_snack_bar_corner)

    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.addView(customSnackView, 0)
    snackBar.show()
}

fun Activity.customSnackBarNormalToastImage(msg: CharSequence) {
    val snackBar: Snackbar = Snackbar.make(this.window.decorView.findViewById(android.R.id.content),
        "", Snackbar.LENGTH_LONG)

    val customSnackView: View = layoutInflater.inflate(R.layout.toast_with_image_standart, null)
    val tvToastSuccess = customSnackView.findViewById<TextView>(R.id.tvToastSuccess)
    tvToastSuccess.text = msg
    tvToastSuccess.gravity = Gravity.START + Gravity.CENTER_VERTICAL

    val layoutParams = snackBar.view.layoutParams as FrameLayout.LayoutParams
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL + Gravity.BOTTOM
    layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT
    layoutParams.leftMargin = 24
    layoutParams.rightMargin = 24
    layoutParams.bottomMargin = 120
    snackBar.view.layoutParams = layoutParams

    snackBar.view.background = ContextCompat.getDrawable(this, R.drawable.bg_normal_toast)
    snackBar.view.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.view.setPadding(0, 0, 0, 0)
//        snackBar.view.translationY = -25f


    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.setPadding(0, 0, 0, 0)
    snackBarLayout.addView(customSnackView, 0)
    snackBar.show()
}

fun Activity.customSnackBarNormalToast(msg: CharSequence) {
    val snackBar: Snackbar = Snackbar.make(this.window.decorView.findViewById(android.R.id.content),
        msg, Snackbar.LENGTH_LONG)

    val layoutParams = snackBar.view.layoutParams as FrameLayout.LayoutParams
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL + Gravity.BOTTOM
    layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT
    layoutParams.leftMargin = 16
    layoutParams.rightMargin = 16
    layoutParams.bottomMargin = 120
    snackBar.view.layoutParams = layoutParams

    snackBar.view.background = ContextCompat.getDrawable(this, R.drawable.bg_normal_toast)
    snackBar.view.textAlignment = View.TEXT_ALIGNMENT_CENTER

    snackBar.show()
}

fun Activity.showCustomNotification(
    activity: Activity,
    @DrawableRes drawable: Int = R.drawable.ic_paotong_onboarding_check_circle,
    message: String,
    marginTop: Int = 0,
    @DrawableRes mainBgId: Int = R.drawable.shape_notification_success
) {
    val snackBar: Snackbar = Snackbar.make(this.window.decorView,
        "", Snackbar.LENGTH_LONG)
    val layout = activity.layoutInflater.inflate(R.layout.layout_notification_view, null) as LinearLayout
    (layout.findViewById<View>(R.id.ivNotificationIcon) as AppCompatImageView).setImageResource(
        drawable
    )
    val textView = layout.findViewById<View>(R.id.tvNotificationMessage) as AppCompatTextView
    textView.text = message
    textView.gravity = Gravity.START + Gravity.CENTER_VERTICAL

    (layout.findViewById<View>(R.id.llNotificationBody) as LinearLayout).background =
        ContextCompat.getDrawable(activity.applicationContext, mainBgId)

    val delegate: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
        override fun onPopulateAccessibilityEvent(host: View, event: AccessibilityEvent) {
            super.onPopulateAccessibilityEvent(host, event)
            event.contentDescription = message
        }
    }

    val layoutParams = snackBar.view.layoutParams as FrameLayout.LayoutParams
    layoutParams.gravity = Gravity.TOP or Gravity.FILL_HORIZONTAL
    layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
    snackBar.view.layoutParams = layoutParams
    snackBar.view.accessibilityDelegate = delegate
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
    snackBarLayout.setOnApplyWindowInsetsListener { view, insets ->
        val top: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insets.getInsets(WindowInsets.Type.systemBars()).top
        } else {
            insets.systemWindowInsetTop
        }
//            view.translationY = (top + marginTop).toFloat()
        view.updatePadding(top = top + marginTop)
        insets
    }
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackBarLayout.addView(layout, 0)
    snackBar.show()
}

fun Activity.showCustomNotificationOld(
    activity: Activity,
    @DrawableRes drawable: Int = R.drawable.ic_paotong_onboarding_check_circle,
    message: String,
    marginTop: Int = 0,
    @DrawableRes mainBgId: Int = R.drawable.shape_notification_success
) {
    val inflater: LayoutInflater = activity.layoutInflater
    val layout = inflater.inflate(R.layout.layout_notification_view, null) as LinearLayout
    (layout.findViewById<View>(R.id.ivNotificationIcon) as AppCompatImageView).setImageResource(
        drawable
    )
    (layout.findViewById<View>(R.id.tvNotificationMessage) as AppCompatTextView).text = message

    (layout.findViewById<View>(R.id.llNotificationBody) as LinearLayout).background =
        ContextCompat.getDrawable(activity.applicationContext, mainBgId)

    val toast = Toast(activity.applicationContext)
    val delegate: View.AccessibilityDelegate = object : View.AccessibilityDelegate() {
        override fun onPopulateAccessibilityEvent(host: View, event: AccessibilityEvent) {
            super.onPopulateAccessibilityEvent(host, event)
            event.contentDescription = message
        }
    }
    toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, marginTop)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout
    (toast.view as ViewGroup?)?.getChildAt(0)?.accessibilityDelegate = delegate
    toast.show()
}