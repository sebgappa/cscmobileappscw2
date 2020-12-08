package com.example.mynewsapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.model.ArticleDto
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.ArticleAdapter
import com.example.mynewsapp.adapters.SavedArticleAdapter
import com.example.mynewsapp.models.ArticleModel
import com.example.mynewsapp.services.FireStoreService
import com.example.mynewsapp.services.NewsAPIService
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.schedulers.Schedulers

class SavedArticlesActivity : AppCompatActivity() {

    private val fireStore = FireStoreService()
    private val newsAPIService = NewsAPIService()
    private val articleCardAdapter = SavedArticleAdapter(
        ArrayList(),
        this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_saved_articles)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        setUpRecyclerView()

        getSavedArticlesBySource()
        getSavedArticlesByCountry()
        getSavedArticlesByTopic()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.headlines -> {
                startActivity(Intent(this, MyNewsPageActivity::class.java))
            }
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteInfoActivity::class.java))
            }
            R.id.local -> {
                startActivity(Intent(this, LocalNewsPageActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() {
        this@SavedArticlesActivity.runOnUiThread {
            val recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
            val layoutManger = LinearLayoutManager(this)
            if (recyclerView != null) {
                recyclerView.layoutManager = layoutManger
            }

            if (recyclerView != null) {
                recyclerView.adapter = articleCardAdapter
            }
        }
    }

    private fun showArticle(article: ArticleDto) {
        this@SavedArticlesActivity.runOnUiThread {
            val myArticle = ArticleModel()
            myArticle.setTitle(article.title)
            myArticle.setDescription(article.description)
            myArticle.setPublishedDate(article.publishedAt)
            myArticle.setUrlToImage(article.urlToImage)
            myArticle.setUrl(article.url)
            myArticle.setPublisher(article.source.name)

            articleCardAdapter.getArticleList().add(myArticle)
            articleCardAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("CheckResult")
    private fun getSavedArticlesByCountry() {
        val result =
            fireStore.getArticlesByCountry(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val mapEntry = task.result!!.data
                if (mapEntry != null) {
                    for (item in mapEntry) {
                        newsAPIService.getNewsByCountry(item.value as String, item.key)
                            .subscribeOn(
                                Schedulers.io()
                            )
                            .toFlowable().subscribe({ articles ->
                                for (article in articles.articles) {
                                    showArticle(article)
                                }
                            }, { error ->
                                Log.d(
                                    "SavedArticlesActivity, error",
                                    error.message.toString()
                                )
                            })
                    }
                }
            } else {
                Log.e("SavedArticlesActivity", task.exception.toString())
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getSavedArticlesByTopic() {
        val result =
            fireStore.getArticlesByTopic(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val mapEntry = task.result!!.data
                if (mapEntry != null) {
                    for (item in mapEntry) {
                        newsAPIService.getNewsByTopic(item.value as String, item.key)
                            .subscribeOn(
                                Schedulers.io()
                            )
                            .toFlowable().subscribe({ articles ->
                                for (article in articles.articles) {
                                    showArticle(article)
                                }
                            }, { error ->
                                Log.d(
                                    "SavedArticlesActivity, error",
                                    error.message.toString()
                                )
                            })
                    }
                }
            } else {
                Log.e("SavedArticlesActivity", task.exception.toString())
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getSavedArticlesBySource() {
        val result =
            fireStore.getArticlesBySource(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val mapEntry = task.result!!.data
                if (mapEntry != null) {
                    for (item in mapEntry) {
                        newsAPIService.getNewsBySource(item.value as String, item.key)
                            .subscribeOn(
                                Schedulers.io()
                            )
                            .toFlowable().subscribe({ articles ->
                                for (article in articles.articles) {
                                    showArticle(article)
                                }
                            }, { error ->
                                Log.d(
                                    "SavedArticlesActivity, error",
                                    error.message.toString()
                                )
                            })
                    }
                }
            } else {
                Log.e("SavedArticlesActivity", task.exception.toString())
            }
        }
    }
}