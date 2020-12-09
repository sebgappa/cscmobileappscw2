package com.example.mynewsapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.ArticlesTabAdapter
import com.example.mynewsapp.models.PreferenceModel
import com.example.mynewsapp.services.FireStoreService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

/**
 * This activity is the main app page, it displays all articles based on user preferences.
 * @author Sebastian Gappa
 */
class MyNewsPageActivity : AppCompatActivity() {

    private val fireStore = FireStoreService()

    /**
     * When the activity is created we call the fireStoreService to ask firebase cloud storage
     * what preferences of news the user has, then we can populate our tabs with this information.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_news_page)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        val preferredNewsTitles = ArrayList<PreferenceModel>()
        val result =
                fireStore.getPreferences(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val mapEntry = task.result!!.data
                if (mapEntry != null) {
                    for (item in mapEntry) {
                        val preferenceModel = PreferenceModel(item.key, item.value.toString())
                        preferredNewsTitles.add(preferenceModel)
                    }
                    setTabTitles(viewPager, tabLayout, preferredNewsTitles)
                }
            } else {
                Log.e("MyNewsPageActivity", task.exception.toString())
            }
        }
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
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteInfoActivity::class.java))
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
     * Sets up activity tabs with news by preferences stored in firebase, creates news fragments
     * which request data from the newsAPI for the current preference.
     */
    private fun setTabTitles(viewPager: ViewPager2, tabLayout: TabLayout, preferredNewsTitles: ArrayList<PreferenceModel>) {
        this@MyNewsPageActivity.runOnUiThread {
            viewPager.adapter =
                    ArticlesTabAdapter(this, preferredNewsTitles)
            TabLayoutMediator(
                    tabLayout,
                    viewPager,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        for (i in preferredNewsTitles.indices) {
                            if (position == i) tab.text = preferredNewsTitles[i].preferenceName
                        }
                    }).attach()
        }
    }
}