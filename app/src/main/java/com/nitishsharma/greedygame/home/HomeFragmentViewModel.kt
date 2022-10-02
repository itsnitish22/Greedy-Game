package com.nitishsharma.greedygame.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitishsharma.greedygame.api.RetrofitInstance
import com.nitishsharma.greedygame.api.models.News
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    //to update the progress dialog
    private val _receivedNews: MutableLiveData<News> = MutableLiveData()
    val receivedNews: LiveData<News>
        get() = _receivedNews

    //function call to GET data from API
    fun getNews(query: String) {
        viewModelScope.launch {
            val fetchedNews =
                RetrofitInstance.api.getNews(query, "619d45f4e8ce4f66b8bdd2db720f24f3")
            _receivedNews.value = fetchedNews
            Log.i("NewsViewModel", fetchedNews.toString())
        }
    }
}