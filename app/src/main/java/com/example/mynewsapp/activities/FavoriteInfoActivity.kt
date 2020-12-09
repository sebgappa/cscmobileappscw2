package com.example.mynewsapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mynewsapp.R
import com.example.mynewsapp.services.FireStoreService
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

/**
 * This activity class allows users to manage their preferences.
 * @author Sebastian Gappa
 */
class FavoriteInfoActivity : AppCompatActivity() {

    private val fireStoreService = FireStoreService()

    /**
     * When the activity is created we call the fireStoreService which retrieves saved preferences
     * from google firebase cloud storage, these preferences are then displayed.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_info)

        val result = fireStoreService.getPreferences(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val mapEntry = task.result!!.data
                if (mapEntry != null) {
                    populatePreferencesLists(mapEntry)
                }
            } else {
                Log.e("FavoriteInfoActivity", task.exception.toString())
            }
        }

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)
    }

    /**
     * Inflates the app toolbar for this activity view.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Listens for which item in the toolbar was pressed and then launches the
     * corresponding activity to navigate.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.headlines -> {
                startActivity(Intent(this, MyNewsPageActivity::class.java))
            }
            R.id.local -> {
                startActivity(Intent(this, LocalNewsPageActivity::class.java))
            }
            R.id.saved -> {
                startActivity(Intent(this, SavedArticlesActivity::class.java))
            }
            R.id.account -> {
                startActivity(Intent(this, AccountActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Launches search activity to look for preferences.
     */
    fun goToSearchPreferences(view: View) {
        startActivity(Intent(this, SearchActivity::class.java))
    }

    /**
     * Populates the listViews with preferences retrieved from firebase cloud storage.
     * Listens for long click to remove preference from listView and firebase.
     */
    private fun populatePreferencesLists(mapEntry: Map<String, Any>) {
        val topicsArray = ArrayList<String>()
        val sourcesArray = ArrayList<String>()
        val countriesArray = ArrayList<String>()

        for (item in mapEntry) {
            when (item.value) {
                "Source" -> sourcesArray.add(item.key)
                "Country" -> countriesArray.add(item.key)
                "Topic" -> topicsArray.add(item.key)
            }
        }

        this@FavoriteInfoActivity.runOnUiThread {
            val topicsAdapter = ArrayAdapter<String>(this,
                    R.layout.preference_list_item, topicsArray)
            val sourcesAdapter = ArrayAdapter<String>(this,
                    R.layout.preference_list_item, sourcesArray)
            val countriesAdapter = ArrayAdapter<String>(this,
                    R.layout.preference_list_item, countriesArray)

            val topicsListView = findViewById<ListView>(R.id.topics_list)
            val sourcesListView = findViewById<ListView>(R.id.sources_list)
            val countriesListView = findViewById<ListView>(R.id.countries_list)

            topicsListView.adapter = topicsAdapter
            sourcesListView.adapter = sourcesAdapter
            countriesListView.adapter = countriesAdapter

            topicsListView.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { _, _, i, _ ->
                        fireStoreService.removePreference(
                                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                                topicsArray[i])

                        topicsAdapter.remove(topicsArray[i])
                        topicsAdapter.notifyDataSetChanged()

                        val parentView: View =
                                this.findViewById<View>(android.R.id.content).rootView
                        this.let {
                            Snackbar.make(
                                    parentView,
                                    "Preference removed!",
                                    Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(ContextCompat.getColor(this, R.color.colorError)).show()
                        }

                        return@OnItemLongClickListener true
                    }

            sourcesListView.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { _, _, i, _ ->
                        fireStoreService.removePreference(
                                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                                sourcesArray[i])

                        sourcesAdapter.remove(sourcesArray[i])
                        sourcesAdapter.notifyDataSetChanged()

                        val parentView: View =
                                this.findViewById<View>(android.R.id.content).rootView
                        this.let {
                            Snackbar.make(
                                    parentView,
                                    "Preference removed!",
                                    Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(ContextCompat.getColor(this, R.color.colorError)).show()
                        }

                        return@OnItemLongClickListener true
                    }

            countriesListView.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { _, _, i, _ ->
                        fireStoreService.removePreference(
                                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                                countriesArray[i])

                        countriesAdapter.remove(countriesArray[i])
                        countriesAdapter.notifyDataSetChanged()

                        val parentView: View =
                                this.findViewById<View>(android.R.id.content).rootView
                        this.let {
                            Snackbar.make(
                                    parentView,
                                    "Preference removed!",
                                    Snackbar.LENGTH_SHORT
                            ).setBackgroundTint(ContextCompat.getColor(this, R.color.colorError)).show()
                        }

                        return@OnItemLongClickListener true
                    }
        }
    }
}