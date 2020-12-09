package com.example.mynewsapp.services

import android.content.res.Resources
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.dfl.newsapi.model.ArticlesDto
import com.dfl.newsapi.model.SourcesDto
import io.reactivex.Single
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

/**
 * This service allows us to make custom queries to the NewsAPI repository.
 * @author Sebastian Gappa
 */
class NewsAPIService
/**
 * On initialisation we create a map of all countries to their ISO codes, the newsAPI
 * repository uses enum values to query by country and we pass in a string so we need
 * to convert between the two somehow.
 */
{
    private val newsApiRepository = NewsApiRepository("aef915265c9943c5a89ed1c09de65f87")
    private val countries: MutableMap<String, String> = HashMap()
    private val defaultPageSize = 30
    private val defaultPage = 1

    init {
        for (iso in Locale.getISOCountries()) {
            val l = Locale("", iso)
            countries[l.displayCountry.toLowerCase(Locale.ROOT)] = iso
        }
    }

    /**
     * Gets all articles by some query.
     */
    fun getNewsByQuery(query: String): Single<ArticlesDto> {
        return newsApiRepository.getEverything(
                q = URLEncoder.encode(query, "utf-8"),
                pageSize = defaultPageSize,
                page = defaultPage
        )
    }

    /**
     * Gets top headlines by a source and nullable query.
     */
    fun getNewsBySource(source: String, query: String?): Single<ArticlesDto> {
        return newsApiRepository.getTopHeadlines(
                q = query,
                sources = normaliseSource(source),
                pageSize = defaultPageSize,
                page = defaultPage
        )
    }

    /**
     * Gets top headlines by a country and nullable query.
     */
    fun getNewsByCountry(country: String, query: String?): Single<ArticlesDto> {
        if (countries[country.toLowerCase(Locale.ROOT)] != null) {
            return newsApiRepository.getTopHeadlines(
                    q = query,
                    /*Here we can query our "HashMap" for the ISO code of our full name country
                    and resolve that as an enum in the newsAPI SDK.*/
                    country = Country.valueOf(countries[country.toLowerCase(Locale.ROOT)]!!),
                    pageSize = defaultPageSize,
                    page = defaultPage
            )
        }

        return Single.error(Resources.NotFoundException())
    }

    /**
     * Gets top headlines by a topic and nullable query.
     */
    fun getNewsByTopic(topic: String, query: String?): Single<ArticlesDto> {
        return newsApiRepository.getTopHeadlines(
                q = query,
                category = Category.valueOf(topic.toUpperCase(Locale.ROOT)),
                country = Country.GB,
                pageSize = defaultPageSize,
                page = defaultPage
        )
    }

    /**
     * Returns general news from the UK.
     */
    fun getDefaultNews(): Single<ArticlesDto> {
        return newsApiRepository.getTopHeadlines(
                category = Category.GENERAL,
                country = Country.GB,
                pageSize = defaultPageSize,
                page = defaultPage
        )
    }

    /**
     * Returns all available sources.
     */
    fun getSources(): Single<SourcesDto> {
        return newsApiRepository.getSources()
    }

    /**
     * The newsAPI requires the source query to be in all lowercase with hyphens delimiting
     * each word.
     */
    private fun normaliseSource(source: String): String {
        return source.replace(' ', '-').toLowerCase(Locale.ROOT)
    }
}