package com.example.mynewsapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.activities.DisplayArticleActivity
import com.example.mynewsapp.models.ArticleModel
import com.example.mynewsapp.services.FireStoreService
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class ArticleAdapter(
    private var articleList: MutableList<ArticleModel>,
    private val currentContext: Context
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val placeHolderImage =
        "https://pbs.twimg.com/profile_images/467502291415617536/SP8_ylk9.png"
    private val fireStore = FireStoreService()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.article_item, parent, false))
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(articleViewHolder: ViewHolder, position: Int) {
        var article: ArticleModel = articleList[position]
        setPropertiesForArticleViewHolder(articleViewHolder, article)
    }

    private fun setPropertiesForArticleViewHolder(
        articleViewHolder: ViewHolder,
        article: ArticleModel
    ) {
        checkForUrlToImage(article, articleViewHolder)
        articleViewHolder.articleTitle.text = article.articleTitle()
        articleViewHolder.articlePublisher.text = article.publisher()
        articleViewHolder.articlePublished.text = article.publishedDate()
        articleViewHolder.articleDescription.text = article.description()

        articleViewHolder.articleCard.setOnClickListener {
            currentContext.startActivity(
                Intent(
                    currentContext,
                    DisplayArticleActivity::class.java
                ).apply {
                    putExtra("url", article.url())
                })
        }
        articleViewHolder.articleCard.setOnLongClickListener {

            saveArticleToFireBase(article)

            return@setOnLongClickListener true
        }
    }

    private fun saveArticleToFireBase(article: ArticleModel) {
        when (article.getPreferenceModel()!!.type) {
            "Source" -> fireStore.saveArticleBySource(
                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                hashMapOf(article.articleTitle() to article.getPreferenceModel()!!.preferenceName!!)
            )
            "Topic" -> fireStore.saveArticleByTopic(
                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                hashMapOf(article.articleTitle() to article.getPreferenceModel()!!.preferenceName!!)
            )
            "Country" -> fireStore.saveArticleByCountry(
                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                hashMapOf(article.articleTitle() to article.getPreferenceModel()!!.preferenceName!!)
            )
        }

        val parentView: View =
            (currentContext as Activity).findViewById<View>(android.R.id.content).rootView

        parentView.let {
            Snackbar.make(
                parentView,
                "Article saved!",
                Snackbar.LENGTH_SHORT
            )
                .setBackgroundTint(
                    ContextCompat.getColor(
                        this.currentContext,
                        R.color.colorSecondary
                    )
                )
                .show()
        }
    }

    private fun checkForUrlToImage(article: ArticleModel, articleViewHolder: ViewHolder) {
        if (article.articleImage().isEmpty()) {
            Picasso.get()
                .load(placeHolderImage)
                .centerCrop()
                .fit()
                .into(articleViewHolder.articleImage)
        } else {
            Picasso.get()
                .load(article.articleImage())
                .centerCrop()
                .fit()
                .into(articleViewHolder.articleImage)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var articleImage: ImageView = itemView.findViewById(R.id.article_image)
        var articleTitle: TextView = itemView.findViewById(R.id.article_title)
        var articlePublisher: TextView = itemView.findViewById(R.id.article_publisher)
        var articlePublished: TextView = itemView.findViewById(R.id.article_published)
        var articleDescription: TextView = itemView.findViewById(R.id.article_description)
        var articleCard: CardView = itemView.findViewById(R.id.article_card_view)

    }
}