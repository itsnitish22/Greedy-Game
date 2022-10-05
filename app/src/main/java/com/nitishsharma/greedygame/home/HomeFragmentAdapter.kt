package com.nitishsharma.greedygame.home

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nitishsharma.greedygame.R
import com.nitishsharma.greedygame.api.models.Articles

class HomeFragmentAdapter(
    private val news: ArrayList<Articles>,
    private val itemClickListener: ItemClickListener,
    private val readClickListener: ItemReadButtonClickListener,
    private val saveClickListener: ItemSaveButtonClickListener,
    private val bookmarkClickListener: ItemBookmarkClickListener
) :
    RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(article: Articles, position: Int)
    }

    interface ItemReadButtonClickListener {
        fun onItemReadClick(article: Articles, position: Int)
    }

    interface ItemSaveButtonClickListener {
        fun onItemSaveClick(article: Articles, position: Int)
    }

    interface ItemBookmarkClickListener {
        fun onBookmarkClickListener(article: Articles, position: Int, sendToDB: Boolean)
    }

    // view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)

        return ViewHolder(view)
    }

    //binding the views with data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = news[position]

        holder.publishedAt.text = itemsViewModel.publishedAt
        holder.title.text = itemsViewModel.title
        holder.description.text = itemsViewModel.description
        Glide.with(holder.itemView.context).load(itemsViewModel.urlToImage).into(holder.newsImage)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(news[position], position)
        }

        holder.readButton.setOnClickListener {
            readClickListener.onItemReadClick(news[position], position)
        }

        holder.saveButton.setOnClickListener {
            saveClickListener.onItemSaveClick(news[position], position)
        }

//        if (selectedIndexes?.contains(position) == true)
//            holder.bookmark.setImageResource(R.drawable.ic_bookmark)

        holder.bookmark.setOnClickListener {
            val res: Resources = holder.itemView.context.resources
            if (holder.bookmark.drawable.constantState ==
                res.getDrawable(R.drawable.ic_bookmark).constantState
            ) {
                holder.bookmark.setImageResource(R.drawable.ic_bookmark_grey)
                bookmarkClickListener.onBookmarkClickListener(news[position], position, false)
            } else {
                holder.bookmark.setImageResource(R.drawable.ic_bookmark)
                bookmarkClickListener.onBookmarkClickListener(news[position], position, true)
            }
        }
    }

    //item count
    override fun getItemCount(): Int {
        return news.size
    }

    //view holder class specifying the views to be used
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.shortDescription)
        val publishedAt: TextView = itemView.findViewById(R.id.publishedAt)
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
        val readButton: Button = itemView.findViewById(R.id.readButton)
        val saveButton: Button = itemView.findViewById(R.id.saveButton)
        val bookmark: ImageView = itemView.findViewById(R.id.bookmarked)
    }
}