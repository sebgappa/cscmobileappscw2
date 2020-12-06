package com.example.mynewsapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.TabAdapter
import com.example.mynewsapp.services.FireStoreService
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MyNewsPageActivity : AppCompatActivity() {

    private val fireStore = FireStoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_news_page)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        var preferredNewsTitles = ArrayList<String>()
        val result =
            fireStore.getPreferences(FirebaseAuth.getInstance().currentUser?.displayName.toString())
        result.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var mapEntry = task.result!!.data
                if (mapEntry != null) {
                    for (item in mapEntry) {
                        preferredNewsTitles.add(item.key)
                    }
                    setTabTitles(viewPager, tabLayout, preferredNewsTitles)
                }
            } else {
                Log.e("MyNewsPageActivity", task.exception.toString())
            }
        }
    }

    private fun setTabTitles(viewPager: ViewPager2, tabLayout: TabLayout, preferredNewsTitles: ArrayList<String>) {
        this@MyNewsPageActivity!!.runOnUiThread {
            viewPager.adapter =
                TabAdapter(this, preferredNewsTitles)
            TabLayoutMediator(
                tabLayout,
                viewPager,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    for (i in preferredNewsTitles.indices) {
                        if (position == i) tab.text = preferredNewsTitles[i]
                    }
                }).attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                startActivity(Intent(this, FavoriteInfoActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}