package com.hoon.tourinkorea

import android.view.View
import com.hoon.tourinkorea.data.model.Post

interface ItemClickListener {
    fun onItemClick(post: Post, view:View)
}