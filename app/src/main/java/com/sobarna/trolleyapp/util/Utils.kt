package com.sobarna.trolleyapp.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun setViewAnimation(
        view: View, alpha: Float, duration: Long
    ) = ObjectAnimator.ofFloat(
        view, View.ALPHA, alpha
    ).setDuration(duration)

    fun setCurrentDate(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)

        return dateFormat.format(currentTimeMillis)
    }

    fun displayImageBitMap(
        context: Context,
        uri: Any,
        imageView: ImageView
    ) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .apply(RequestOptions().fitCenter())
            .into(imageView)
    }


    fun displayImageDrawable(
        context: Context,
        drawable: Int,
        imageView: ImageView
    ) {
        Glide.with(context)
            .asDrawable()
            .load(drawable)
            .apply(RequestOptions().centerCrop().override(imageView.width, imageView.height))
            .into(imageView)
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus

        if (view == null) view = View(activity)

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}