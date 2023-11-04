package com.hoon.tourinkorea

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        view.load(it) {
            crossfade(true)
        }
    }
}