package com.nitishsharma.greedygame.api.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Articles(
    val source: SourceArticle,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
) : Parcelable