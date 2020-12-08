package com.example.mynewsapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.mynewsapp.R
import com.example.mynewsapp.adapters.ArticlesTabAdapter
import com.example.mynewsapp.models.PreferenceModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException
import java.util.*

class LocalNewsPageActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
                if (permissionGranted) {
                    resolveLocation()
                } else {
                    showGeneralNews()
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_local_news_page)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            resolveLocation()
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
            R.id.saved -> {
                startActivity(Intent(this, SavedArticlesActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTabTitles(preferredNewsTitles: ArrayList<PreferenceModel>) {
        val tabLayout = findViewById<TabLayout>(R.id.local_news_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.local_news_view_pager)

        this@LocalNewsPageActivity.runOnUiThread {
            viewPager.adapter =
                    ArticlesTabAdapter(this, preferredNewsTitles)
            TabLayoutMediator(
                    tabLayout,
                    viewPager,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when (position) {
                            0 -> tab.text = "Country"
                            1 -> tab.text = "City"
                        }
                    }).attach()
        }
    }

    private fun resolveLocation() {
        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = PRIORITY_HIGH_ACCURACY
        try {
            val mLocationCallback: LocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult != null) {
                        for (location in locationResult.locations) {
                            fusedLocationClient.lastLocation
                                    .addOnSuccessListener { lastLocation: Location? ->
                                        if (lastLocation != null) {
                                            val address = resolveAddressFromLocation(location.longitude, location.latitude)
                                            setTabTitles(arrayListOf(
                                                    PreferenceModel(address?.get(0), "Country"),
                                                    PreferenceModel(address?.get(1), "Query")))
                                        }
                                    }
                        }
                    } else {
                        showGeneralNews()
                    }
                }
            }

            LocationServices
                    .getFusedLocationProviderClient(this)
                    .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        } catch (e: SecurityException) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun resolveAddressFromLocation(longitude: Double, latitude: Double): Array<String>? {
        val geoCoder = Geocoder(this, Locale.getDefault())

        return try {
            val address: List<Address> =
                    geoCoder.getFromLocation(latitude, longitude, 1)

            arrayOf(address[0].countryName, address[0].subAdminArea)
        } catch (e: IOException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

    private fun showGeneralNews() {
        setTabTitles(arrayListOf(PreferenceModel("", "")))
    }
}