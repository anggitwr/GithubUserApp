package com.anggitdev.myapplication

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

fun String.firstUpper() =
    this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }

fun ImageView.loadImage(url: String, width: Int = 500, height: Int = 500) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().override(width, height))
        .error(R.drawable.item_background)
        .centerCrop()
        .into(this)
}