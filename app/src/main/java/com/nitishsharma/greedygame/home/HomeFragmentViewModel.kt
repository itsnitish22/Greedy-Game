package com.nitishsharma.greedygame.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nitishsharma.greedygame.api.RetrofitInstance
import com.nitishsharma.greedygame.api.models.Articles
import com.nitishsharma.greedygame.api.models.News
import com.nitishsharma.greedygame.database.AppDatabase
import com.nitishsharma.greedygame.database.NewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)

    //to update the progress dialog
    private val _receivedNews: MutableLiveData<News> = MutableLiveData()
    val receivedNews: LiveData<News>
        get() = _receivedNews

//    private val _newsFromDB: MutableLiveData<NewsEntity> = MutableLiveData()
//    val newsFromDB: LiveData<NewsEntity>
//        get() = _newsFromDB

    //function call to GET data from API
    fun getNews(query: String) {
        viewModelScope.launch {
            val fetchedNews =
                RetrofitInstance.api.getNews(query, "619d45f4e8ce4f66b8bdd2db720f24f3")
            _receivedNews.value = fetchedNews
//            Log.i("NewsViewModel", fetchedNews.toString())
        }
    }

    //saving news into DB
    fun sendNewsToDB(article: Articles, position: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newsToInsert = NewsEntity(
                    position,
                    article.title,
                    article.publishedAt,
                    article.source.name,
                    article.description,
                    article.urlToImage
                )
                database?.newsDao()?.insertNews(newsToInsert)
            }
        }
    }

    //getAllNewsFromDB
    val newsListFromDB = database?.newsDao()?.getAll()

    fun deleteNews(article: Articles, position: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val newsToDelete = NewsEntity(
                    position,
                    article.title,
                    article.publishedAt,
                    article.source.name,
                    article.description,
                    article.urlToImage
                )
                database?.newsDao()?.deleteNote(newsToDelete)
            }
        }
    }
}