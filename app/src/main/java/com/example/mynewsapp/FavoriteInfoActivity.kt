package com.example.mynewsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FavoriteInfoActivity: AppCompatActivity() {

    private val fireStore =  FireStoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_info)

        val result = fireStore.getPreferences(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                var mapEntry = task.result!!.data
                if(mapEntry != null) {
                    populatePreferencesLists(mapEntry)
                }
            } else {
                Log.e("FavoriteInfoActivity", task.exception.toString())
            }
        }

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)
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

            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToSearchPreferences(view: View) {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    private fun populatePreferencesLists(mapEntry: Map<String, Any>) {
        var topicsArray = ArrayList<String>()
        var sourcesArray = ArrayList<String>()
        var countriesArray = ArrayList<String>()

        for(item in mapEntry) {
            when(item.value) {
                "Source" -> sourcesArray.add(item.key)
                "Country" -> countriesArray.add(item.key)
                "Topic" -> topicsArray.add(item.key)
            }
        }

        this@FavoriteInfoActivity!!.runOnUiThread(Runnable {
            val topicsAdapter = ArrayAdapter<String>(this, R.layout.preference_list_item, topicsArray)
            val sourcesAdapter = ArrayAdapter<String>(this, R.layout.preference_list_item, sourcesArray)
            val countriesAdapter = ArrayAdapter<String>(this, R.layout.preference_list_item, countriesArray)

            val topicsListView = findViewById<ListView>(R.id.topics_list)
            val sourcesListView = findViewById<ListView>(R.id.sources_list)
            val countriesListView = findViewById<ListView>(R.id.countries_list)

            topicsListView.adapter = topicsAdapter
            sourcesListView.adapter = sourcesAdapter
            countriesListView.adapter = countriesAdapter
        })
    }
}