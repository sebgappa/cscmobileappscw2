package com.example.mynewsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class FavoriteInfoActivity: AppCompatActivity() {

    private val fireStore =  FireStoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_info)

        val topicsArray = arrayOf("Science", "Technology", "Business", "Politics")
        val sourcesArray = arrayOf("Daily Express", "The Guardian", "Sky News")
        val countriesArray = arrayOf("UK", "Germany")

        val topicsAdapter = ArrayAdapter<String>(this, R.layout.list_item, topicsArray)
        val sourcesAdapter = ArrayAdapter<String>(this, R.layout.list_item, sourcesArray)
        val countriesAdapter = ArrayAdapter<String>(this, R.layout.list_item, countriesArray)

        val topicsListView = findViewById<ListView>(R.id.topics_list)
        val sourcesListView = findViewById<ListView>(R.id.sources_list)
        val countriesListView = findViewById<ListView>(R.id.countries_list)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)

        topicsListView.adapter = topicsAdapter
        sourcesListView.adapter = sourcesAdapter
        countriesListView.adapter = countriesAdapter
        setSupportActionBar(mainToolbar)

        val test = fireStore.getPreferences(FirebaseAuth.getInstance().currentUser?.displayName.toString(), "Source")
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
}