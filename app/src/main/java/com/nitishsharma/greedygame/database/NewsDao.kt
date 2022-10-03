package com.nitishsharma.greedygame.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: NewsEntity)

    @Query("SELECT * FROM news ORDER BY id ASC")
    fun getAll(): LiveData<List<NewsEntity>>

    @Delete
    fun deleteNote(news: NewsEntity)
}