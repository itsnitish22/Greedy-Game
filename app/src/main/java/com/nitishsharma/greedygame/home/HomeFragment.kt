package com.nitishsharma.greedygame.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nitishsharma.greedygame.api.models.Articles
import com.nitishsharma.greedygame.api.models.News
import com.nitishsharma.greedygame.databinding.FragmentHomeBinding
import kotlinx.coroutines.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var adapter: RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>? = null //adapter
    private lateinit var viewModel: HomeFragmentViewModel
    private val newsList: ArrayList<Articles> = arrayListOf() //news
    private lateinit var searchView: SearchView
    private var search: String = "tesla"
    private val savedNewsBookmarkIndex: MutableList<Int>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        searchView = binding.searches
        binding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext()) //setting recycler view

        //observing instance state
        val selectedNews = savedInstanceState?.getIntegerArrayList("saved")
        if (selectedNews?.isNotEmpty() == true)
            Log.i("HomeFrag", selectedNews.toString())


        //getting news from api
        viewModel.getNews("tesla")
        binding.progressBar.visibility = View.VISIBLE

        binding.bookmarks.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSavedNews())
        }

        //search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
            private var searchJob: Job? = null
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    search = query
                    viewModel.getNews(query)
                    binding.progressBar.visibility = View.VISIBLE
                }
                return false
            }

            //changing text and setting default search to tesla
            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    newText?.let {
                        delay(1000)
                        if (it.isEmpty()) {
                            viewModel.getNews("tesla")
                            binding.progressBar.visibility = View.VISIBLE
                        } else {
                            search = it
                            viewModel.getNews(it)
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
                return false
            }
        })

        //observing news changes from api
        viewModel.receivedNews.observe(requireActivity()) { news ->
            putNewsIntoArray(news)
        }
        return binding.root
    }

    //putting data received in an array for sending to the adapter
    private fun putNewsIntoArray(news: News?) {
        newsList.clear()
        if (news != null) {
            for (i in 0 until news.articles.size) {
                newsList.add(news.articles[i])
            }
            showInRecyclerView(newsList)
        }
    }

    //showing data in recycler view
    private fun showInRecyclerView(newsList: ArrayList<Articles>) {
        adapter = HomeFragmentAdapter(newsList, object : HomeFragmentAdapter.ItemClickListener {
            override fun onItemClick(article: Articles, position: Int) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailedNews(
                        article
                    )
                )
            }

        }, object : HomeFragmentAdapter.ItemReadButtonClickListener {
            override fun onItemReadClick(article: Articles, position: Int) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailedNews(
                        article
                    )
                )
            }

        }, object : HomeFragmentAdapter.ItemSaveButtonClickListener {
            override fun onItemSaveClick(article: Articles, position: Int) {
                savedNewsBookmarkIndex?.add(position + 1)
                viewModel.sendNewsToDB(article, position + 1)
                Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show()
            }

        },
            object : HomeFragmentAdapter.ItemBookmarkClickListener {
                override fun onBookmarkClickListener(
                    article: Articles,
                    position: Int,
                    sendToDB: Boolean
                ) {
                    if (sendToDB) {
                        savedNewsBookmarkIndex?.add(position + 1)
                        viewModel.sendNewsToDB(article, position + 1)
                        Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        savedNewsBookmarkIndex?.remove(position + 1)
                        viewModel.deleteNews(article, position + 1)
                        Toast.makeText(activity, "Unsaved", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        binding.recyclerview.adapter = adapter
        binding.progressBar.visibility = View.GONE
    }

    //saving instance state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("HomeFrag", savedNewsBookmarkIndex.toString())
        if (savedNewsBookmarkIndex?.isNotEmpty() == true)
            outState.putIntegerArrayList(
                "saved",
                savedNewsBookmarkIndex as java.util.ArrayList<Int>?
            )
    }
}