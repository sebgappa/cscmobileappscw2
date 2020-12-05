package com.example.mynewsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.dfl.newsapi.model.ArticlesDto
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.schedulers.Schedulers

class MyNewsPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_news_page)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        //fetchNewsArticles()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteInfoActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchNewsArticles() {
        val newsApiRepository = NewsApiRepository("f1010fbbbebb48aba026e6c2c47cc9e2")

        newsApiRepository.getTopHeadlines(
                category = Category.GENERAL,
                country = Country.GB,
                pageSize = 20,
                page = 1).subscribeOn(Schedulers.io())
                .toFlowable().subscribe({ articles ->
                    populateArticles(articles)
                }, { t -> Log.d("getTopHeadlines error", t.message.toString()) })
    }

    private fun populateArticles(articles: ArticlesDto) {
        this@MyNewsPageActivity.runOnUiThread {
            var articleList = ArrayList<Article>()
            for (article in articles.articles) {
                var myArticle = Article()
                myArticle.setTitle(article.title)
                myArticle.setDescription(article.description)
                myArticle.setPublishedDate(article.publishedAt)
                myArticle.setUrlToImage(article.urlToImage)
                myArticle.setUrl(article.url)
                myArticle.setPublisher(article.source.name)
                articleList.add(myArticle)
            }

            val recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
            val layoutManger = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManger
            val articleCardAdapter = ArticleAdapter(articleList, this)
            recyclerView.adapter = articleCardAdapter
        }
    }
}