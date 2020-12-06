package com.example.mynewsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.dfl.newsapi.model.ArticlesDto
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.ArticleAdapter
import com.example.mynewsapp.models.ArticleModel
import io.reactivex.schedulers.Schedulers

class NewsFragment(private val contentType: String) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_news, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fetchNewsArticles()
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
        activity?.runOnUiThread {
            var articleList = ArrayList<ArticleModel>()
            for (article in articles.articles) {
                var myArticle = ArticleModel()
                myArticle.setTitle(article.title)
                myArticle.setDescription(article.description)
                myArticle.setPublishedDate(article.publishedAt)
                myArticle.setUrlToImage(article.urlToImage)
                myArticle.setUrl(article.url)
                myArticle.setPublisher(article.source.name)
                articleList.add(myArticle)
            }

            val recyclerView = view?.findViewById<RecyclerView>(R.id.main_recycler_view)
            val layoutManger = LinearLayoutManager(activity!!)
            if (recyclerView != null) {
                recyclerView.layoutManager = layoutManger
            }
            val articleCardAdapter =
                ArticleAdapter(
                    articleList,
                    activity!!
                )
            if (recyclerView != null) {
                recyclerView.adapter = articleCardAdapter
            }
        }
    }
}