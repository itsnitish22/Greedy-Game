package com.nitishsharma.greedygame.database

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nitishsharma.greedygame.R

class SavedNewsAdapter(private val news: List<NewsEntity>?) :
    RecyclerView.Adapter<SavedNewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = news?.get(position)

        holder.publishedAt.text = itemsViewModel?.publishedAt
        holder.title.text = itemsViewModel?.title
        holder.description.text = itemsViewModel?.description
        holder.newsImage.clipToOutline = true
        Glide.with(holder.itemView.context).load(itemsViewModel?.urlToImage).into(holder.newsImage)
    }

    override fun getItemCount(): Int {
        Log.i("Saved Adapter", news?.size.toString())
        return news?.size ?: 0
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.shortDescription)
        val publishedAt: TextView = itemView.findViewById(R.id.publishedAt)
        val newsImage: ImageView = itemView.findViewById(R.id.newsImage)
    }
}
