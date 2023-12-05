package com.hoon.tourinkorea.ui

import com.hoon.tourinkorea.data.model.Post

interface ItemClickListener {
    fun onItemClick(post: Post)
}