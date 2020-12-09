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

/**
 * First bonus feature, this activity retrieves local news information based on the users
 * last known location.
 * @author Sebastian Gappa
 */
class LocalNewsPageActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Launches android permissions activity to ask the user for location permission. If
     * permission isn't granted show general news.
     */
    private val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
                if (permissionGranted) {
                    resolveLocation()
                } else {
                    showGeneralNews()
                }
            }

    /**
     * Checks if the app has permission to access location data then tries to resolve last known
     * location, otherwise requests for permission.
     */
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
            R.id.headlines -> {
                startActivity(Intent(this, MyNewsPageActivity::class.java))
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
     * Sets up activity tabs with local news by "Country" and "City", creates news fragments
     * which request data from the news API for the current city and country.
     */
    private fun setTabTitles(preferredNewsTitles: ArrayList<PreferenceModel>) {
        this@LocalNewsPageActivity.runOnUiThread {
            val tabLayout = findViewById<TabLayout>(R.id.local_news_tab_layout)
            val viewPager = findViewById<ViewPager2>(R.id.local_news_view_pager)

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

    /**
     * "fusedLocationProviderClient" can only resolve last known location if there is already an
     * existing client which has made a request, this function will make a request for the users
     * location and then resolve it using the client into an address which can be broken down into
     * city and country.
     */
    private fun resolveLocation() {
        this@LocalNewsPageActivity.runOnUiThread {
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
    }

    /**
     * Converts longitude and latitude coordinates into usable address information for current city
     * and country.
     */
    private fun resolveAddressFromLocation(longitude: Double, latitude: Double): Array<String>? {
        val geoCoder = Geocoder(this, Locale.getDefault())

        return try {
            val address: List<Address> =
                    geoCoder.getFromLocation(latitude, longitude, 1)

            /* We only need to return the first address near this location, they will all have the
            same city and country */
            arrayOf(address[0].countryName, address[0].subAdminArea)
        } catch (e: IOException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

    /**
     * If the user doesn't want to grant permissions show general news
     */
    private fun showGeneralNews() {
        setTabTitles(arrayListOf(PreferenceModel("", "")))
    }
}