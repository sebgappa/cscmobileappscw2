package com.example.mynewsapp.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.SearchListViewAdapter
import com.example.mynewsapp.models.PreferenceModel
import com.example.mynewsapp.services.FireStoreService
import com.google.firebase.auth.FirebaseAuth
import java.text.Normalizer
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity: AppCompatActivity() {

    private var listView: ListView? = null
    private var searchResults = ArrayList<PreferenceModel>()
    private var adapter: SearchListViewAdapter? = null
    private val fireStore = FireStoreService()

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
                search(query)
            }
        }
    }

    private fun search(query: String) {
        val searchData = arrayOf(
            arrayOf("United Kingdom", "Country"),
            arrayOf("Science", "Topic"),
            arrayOf("BBC News", "Source"),
            arrayOf("Google News", "Source"),
            arrayOf("Technology", "Topic"),
            arrayOf("Economics", "Topic"),
            arrayOf("General", "Topic"),
            arrayOf("Health", "Topic"),
            arrayOf("Entertainment", "Topic"),
            arrayOf("ITV News", "Source"),
            arrayOf("Daily Mail", "Source"),
            arrayOf("The Guardian", "Source"),
            arrayOf("Germany", "Country"),
            arrayOf("France", "Country"),
            arrayOf("United States", "Country"),
            arrayOf("Belgium", "Country")
        )

        val normalisedQuery = normalise(query)

        for (item in searchData) {

            val normalisedItem = normalise(item[0])

            if (normalisedQuery.all { term -> normalisedItem.any { word -> word.contains(term) } }) {
                searchResults.add(
                    PreferenceModel(
                        item[0],
                        item[1]
                    )
                )
            }
        }

        adapter?.notifyDataSetChanged()
    }

    private fun normalise(query: String): List<String> {
        val list = Normalizer.normalize(query.toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "")
            .split(" ")
            .toMutableList()

        for (word in list) if (word == " ") list.remove(word)

        return list
    }
}