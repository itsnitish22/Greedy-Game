package com.nitishsharma.greedygame.database

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nitishsharma.greedygame.databinding.FragmentSavedNewsBinding
import com.nitishsharma.greedygame.home.HomeFragmentViewModel

class SavedNews : Fragment() {
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private var adapter: RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>? = null //adapter
    private val newsList: ArrayList<NewsEntity> = arrayListOf() //news

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        binding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext())

        viewModel.newsListFromDB?.observe(requireActivity()) { it ->
            Log.i("Saved", it.toString())
            binding.progressBar.visibility = View.VISIBLE
            sendToRecyclerView(it)
        }

        binding.backarrow.setOnClickListener {
            findNavController().navigate(SavedNewsDirections.actionSavedNewsToHomeFragment())
        }
        return binding.root
    }

    private fun sendToRecyclerView(it: List<NewsEntity>?) {
        newsList.clear()
        Log.i("Saved", it.toString())
        adapter = SavedNewsAdapter(it)
        binding.recyclerview.adapter = adapter
        binding.progressBar.visibility = View.GONE
    }

}