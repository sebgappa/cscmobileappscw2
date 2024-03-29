package com.example.mynewsapp.services

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.dfl.newsapi.model.ArticlesDto
import com.example.mynewsapp.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Single
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

class NewsAPIService {
    private val newsApiRepository = NewsApiRepository("f1010fbbbebb48aba026e6c2c47cc9e2")
    private val countries: MutableMap<String, String> = HashMap()
    private val defaultPageSize = 30
    private val defaultPage = 1

    constructor() {
        for (iso in Locale.getISOCountries()) {
            val l = Locale("", iso)
            countries[l.displayCountry.toLowerCase()] = iso
        }
    }

    fun getNewsByQuery(query: String): Single<ArticlesDto> {
        return newsApiRepository.getEverything(
            q = URLEncoder.encode(query, "utf-8"),
            pageSize = defaultPageSize,
            page = defaultPage
        )
    }

    fun getNewsBySource(source: String, query: String?): Single<ArticlesDto> {
        return newsApiRepository.getTopHeadlines(
            q = query,
            sources = normaliseSource(source),
            pageSize = defaultPageSize,
            page = defaultPage
        )
    }

    fun getNewsByCountry(country: String, query: String?): Single<ArticlesDto> {
        if (countries[country.toLowerCase()] != null) {
            return newsApiRepository.getTopHeadlines(
                q = query,
                country = Country.valueOf(countries[country.toLowerCase()]!!),
                pageSize = defaultPageSize,
                page = defaultPage
            )
        }

        return Single.error(Resources.NotFoundException())
    }

    fun getNewsByTopic(topic: String, query: String?): Single<ArticlesDto> {
        return newsApiRepository.getTopHeadlines(
            q = query,
            category = Category.valueOf(topic.toUpperCase()),
            country = Country.GB,
            pageSize = defaultPageSize,
            page = defaultPage
        )
    }

    fun getDefaultNews(): Single<ArticlesDto> {
        return newsApiRepository.getTopHeadlines(
            category = Category.GENERAL,
            country = Country.GB,
            pageSize = defaultPageSize,
            page = defaultPage
        )
    }

    private fun normaliseSource(source: String): String {
        return source.replace(' ', '-').toLowerCase()
    }
}