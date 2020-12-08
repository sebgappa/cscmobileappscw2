package com.example.mynewsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.model.ArticlesDto
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.ArticleAdapter
import com.example.mynewsapp.models.ArticleModel
import com.example.mynewsapp.models.PreferenceModel
import com.example.mynewsapp.services.NewsAPIService
import com.google.android.material.snackbar.Snackbar
import io.reactivex.schedulers.Schedulers

class NewsFragment(private val preferenceModel: PreferenceModel) : Fragment() {
    private val newsAPIService = NewsAPIService()

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

        getNewsArticles()
    }

    private fun getNewsArticles() {

        when (preferenceModel.type) {
            "Country" -> newsAPIService.getNewsByCountry(preferenceModel.preferenceName!!)
                .subscribeOn(Schedulers.io())
                .toFlowable().subscribe({ articles ->
                    populateArticles(articles)
                }, { error ->
                    handleNewsAPIError(R.string.country_not_found.toString())
                    Log.d(
                        "NewsFragment, getNewsByCountry() error",
                        error.message.toString()
                    )
                })

            "Source" -> newsAPIService.getNewsBySource(preferenceModel.preferenceName!!)
                .subscribeOn(Schedulers.io())
                .toFlowable().subscribe({ articles ->
                    populateArticles(articles)
                }, { error ->
                    handleNewsAPIError(R.string.source_not_found.toString())
                    Log.d(
                        "NewsFragment, getNewsBySource() error",
                        error.message.toString()
                    )
                })
            "Topic" -> newsAPIService.getNewsByTopic(preferenceModel.preferenceName!!)
                .subscribeOn(Schedulers.io())
                .toFlowable().subscribe({ articles ->
                    populateArticles(articles)
                }, { error ->
                    handleNewsAPIError(R.string.topic_not_found.toString())
                    Log.d(
                        "NewsFragment, getNewsByTopic() error",
                        error.message.toString()
                    )
                })
            "Query" -> newsAPIService.getNewsByQuery(preferenceModel.preferenceName!!)
                .subscribeOn(Schedulers.io())
                .toFlowable().subscribe({ articles ->
                    populateArticles(articles)
                }, { error ->
                    handleNewsAPIError(R.string.topic_not_found.toString())
                    Log.d(
                        "NewsFragment, getNewsByTopic() error",
                        error.message.toString()
                    )
                })
            else -> newsAPIService.getDefaultNews()
                .subscribeOn(Schedulers.io())
                .toFlowable().subscribe({ articles ->
                    populateArticles(articles)
                }, { error ->
                    Log.d(
                        "NewsFragment, getDefaultNews() error",
                        error.message.toString()
                    )
                })
        }
    }

    private fun populateArticles(articles: ArticlesDto) {
        activity?.runOnUiThread {
            val articleList = ArrayList<ArticleModel>()
            for (article in articles.articles) {
                val myArticle = ArticleModel()
                myArticle.setTitle(article.title)
                myArticle.setDescription(article.description)
                myArticle.setPublishedDate(article.publishedAt)
                myArticle.setUrlToImage(article.urlToImage)
                myArticle.setUrl(article.url)
                myArticle.setPublisher(article.source.name)
                articleList.add(myArticle)
            }

            val recyclerView = view?.findViewById<RecyclerView>(R.id.main_recycler_view)
            val layoutManger = LinearLayoutManager(requireActivity())
            if (recyclerView != null) {
                recyclerView.layoutManager = layoutManger
            }
            val articleCardAdapter =
                ArticleAdapter(
                    articleList,
                    requireActivity()
                )
            if (recyclerView != null) {
                recyclerView.adapter = articleCardAdapter
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun handleNewsAPIError(displayMessage: String) {
        view?.let {
            Snackbar.make(
                it,
                displayMessage,
                Snackbar.LENGTH_SHORT
            )
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorError))
                .show()
        }

        newsAPIService.getDefaultNews()
            .subscribeOn(Schedulers.io())
            .toFlowable().subscribe({ articles ->
                populateArticles(articles)
            }, { error ->
                Log.d(
                    "NewsFragment, handleNewsAPIError() error",
                    error.message.toString()
                )
            })
    }
}