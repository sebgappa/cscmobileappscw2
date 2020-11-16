package com.example.mynewsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ArticleCardAdapter(private val articleCardList: MutableList<ArticleCardModel>)
    : RecyclerView.Adapter<ArticleCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.article_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = articleCardList[position]

        holder.articleImage.setImageResource(info.getArticleImage())
        holder.articleTitle.text = info.getArticleTitle()
        holder.articlePublisherImage.setImageResource(info.getPublisherImage())
        holder.articlePublisher.text = info.getPublisher()
        holder.articlePublished.text = info.getPublishedDate()
        holder.articleDescription.text = info.getArticleDescription()
    }

    override fun getItemCount(): Int {
        return articleCardList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val articleImage: ImageView = itemView.findViewById(R.id.article_image)
        val articleTitle: TextView = itemView.findViewById(R.id.article_title)
        val articlePublisherImage: ImageView = itemView.findViewById(R.id.article__publisher_image)
        var articlePublisher: TextView = itemView.findViewById(R.id.article_publisher)
        var articlePublished: TextView = itemView.findViewById(R.id.article_published)
        var articleDescription: TextView = itemView.findViewById(R.id.article_description)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Snackbar.make(v, "${articleTitle.text} has been clicked!", Snackbar.LENGTH_LONG)
        }
    }
}