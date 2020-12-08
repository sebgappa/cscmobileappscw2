package com.example.mynewsapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.TabAdapter
import com.example.mynewsapp.models.PreferenceModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class LocalNewsPageActivity: AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_local_news_page)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        val tabLayout = findViewById<TabLayout>(R.id.local_news_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.local_news_view_pager)

        var preferredNewsTitles = arrayListOf(
            PreferenceModel("Swansea", "Query"),
            PreferenceModel("United Kingdom", "Country")
        )

        setTabTitles(viewPager, tabLayout, preferredNewsTitles)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                var test = location.toString()
            }
    }

    private fun setTabTitles(viewPager: ViewPager2, tabLayout: TabLayout, preferredNewsTitles: ArrayList<PreferenceModel>) {
        this@LocalNewsPageActivity!!.runOnUiThread {
            viewPager.adapter =
                TabAdapter(this, preferredNewsTitles)
            TabLayoutMediator(
                tabLayout,
                viewPager,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    when(position) {
                        0 -> tab.text = "City"
                        1 -> tab.text = "Country"
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
}