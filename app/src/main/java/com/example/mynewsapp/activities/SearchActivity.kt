package com.example.mynewsapp.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.dfl.newsapi.model.SourcesDto
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.SearchListViewAdapter
import com.example.mynewsapp.models.PreferenceModel
import com.example.mynewsapp.services.FireStoreService
import com.example.mynewsapp.services.NewsAPIService
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.schedulers.Schedulers
import java.text.Normalizer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A search interface activity, we can query a data set of preferences to save to firebase.
 * @author Sebastian Gappa
 */
class SearchActivity(): AppCompatActivity() {

    private var listView: ListView? = null
    private var searchResults = ArrayList<PreferenceModel>()
    private var adapter: SearchListViewAdapter? = null
    private val fireStore = FireStoreService()
    private val newsAPIService = NewsAPIService()

    /**
     * Inflates the search widget on the page and waits for search results to be populated.
     * Then we can save the clicked search result to firebase to read in our "FavoriteInfoActivity".
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        findViewById<SearchView>(R.id.search_widget).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        listView = findViewById(R.id.search_list_view)
        adapter = SearchListViewAdapter(
            this,
            searchResults
        )
        (listView as ListView).adapter = adapter

        (listView as ListView).onItemClickListener =
                AdapterView.OnItemClickListener { _, _, i, _ ->
                    val preferenceType: Any = searchResults[i].type!!
                    val preferenceName: String = searchResults[i].preferenceName!!

                    fireStore.savePreferences(
                            FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                            hashMapOf(preferenceName to preferenceType))

                    startActivity(Intent(this, FavoriteInfoActivity::class.java))
                }

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                newsAPIService.getSources()
                        .subscribeOn(
                                Schedulers.io()
                        )
                        .toFlowable().subscribe({ sources ->
                            search(query, sources)
                        }, { error ->
                            Log.d("SavedArticlesActivity, error", error.message.toString()
                            )
                        })
            }
        }
    }

    /**
     * Compares normalised search query and data to check for full or partial match.
     */
    private fun search(query: String, sources: SourcesDto) {
        var searchData: HashMap<String, String> = HashMap()
        searchData = populateSourceSearchData(searchData, sources)
        searchData = populateCountrySearchData(searchData)
        searchData = populateTopicSearchData(searchData)

        val normalisedQuery = normalise(query)

        for (item in searchData.keys) {
            val normalisedItem = normalise(item)

            if (normalisedQuery.all { term -> normalisedItem.any { word -> word.contains(term) } }) {
                searchResults.add(
                        PreferenceModel(
                                item,
                                searchData[item]
                        )
                )
            }
        }

        adapter?.notifyDataSetChanged()
    }

    /**
     * Populates search data for all sources from newsAPI
     */
    private fun populateSourceSearchData(searchData: HashMap<String, String>, sources: SourcesDto): HashMap<String, String> {
        for (source in sources.sources) {
            searchData[source.name] = "Source"
        }

        return searchData
    }

    /**
     * Populates search data for all countries for ISO locale
     */
    private fun populateCountrySearchData(searchData: HashMap<String, String>): HashMap<String, String> {
        val countries = ArrayList<String>()
        for (iso in Locale.getISOCountries()) {
            val l = Locale("", iso)
            countries.add(l.displayCountry)
        }

        for (country in countries) {
            searchData[country] = "Country"
        }

        return searchData
    }

    /**
     * Populates search data for all queryable topics to the newsAPI
     */
    private fun populateTopicSearchData(searchData: HashMap<String, String>): HashMap<String, String> {
        val topics = arrayOf("Business", "Entertainment", "General", "Health", "Science", "Sports" ,"Technology")

        for (topic in topics) {
            searchData[topic] = "Topic"
        }

        return searchData
    }

    /**
     * Normalises string into a list of complete lowercase words.
     */
    private fun normalise(query: String): List<String> {
        val list = Normalizer.normalize(query.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "")
            .split(" ")
            .toMutableList()

        for (word in list) if (word == " ") list.remove(word)

        return list
    }
}