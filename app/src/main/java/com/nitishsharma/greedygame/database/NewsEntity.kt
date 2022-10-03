package com.nitishsharma.greedygame.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    val title: String?,
    val publishedAt: String?,
    val source: String?,
    val description: String?,
    val urlToImage: String?
)