package com.nitishsharma.greedygame.detailed_news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nitishsharma.greedygame.api.models.Articles
import com.nitishsharma.greedygame.databinding.FragmentDetailedNewsBinding

class DetailedNews : Fragment() {
    private lateinit var binding: FragmentDetailedNewsBinding
    private val args: DetailedNewsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedNewsBinding.inflate(inflater, container, false)

        showInFragment(args.article)
        Log.i("DetailedNews", args.article.toString())

        return binding.root
    }

    private fun showInFragment(article: Articles?) {
        if (article != null) {
            binding.publishedAt.text = article.publishedAt
            binding.title.text = article.title
            binding.authorName.text = article.author
            binding.source.text = article.source.name
            binding.content.text = article.content

            activity?.let { Glide.with(it).load(article.urlToImage).into(binding.picture) }
        }
    }

}