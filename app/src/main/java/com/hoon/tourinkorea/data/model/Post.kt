package com.hoon.tourinkorea.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Post(
    val title: String,
    val location: String,
    val description: String,
    val storageUriList: List<String>,
    val publishedAt: String,
    var downloadUrls: List<String> = emptyList()
): Parcelable