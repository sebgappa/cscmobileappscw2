package com.example.mynewsapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_account)

        val mainToolbar = findViewById<Toolbar>(R.id.main_appbar)
        setSupportActionBar(mainToolbar)

        val profilePicture = findViewById<ImageView>(R.id.profilePicture)
        Picasso.get()
                .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
                .resize(300, 300)
                .placeholder(R.drawable.googleicon)
                .into(profilePicture)

        val accountName = findViewById<TextView>(R.id.name_value)
        accountName.text = FirebaseAuth.getInstance().currentUser?.displayName

        val accountEmailAddress = findViewById<TextView>(R.id.email_address_value)
        accountEmailAddress.text = FirebaseAuth.getInstance().currentUser?.email

        val accountPhoneNumber = findViewById<TextView>(R.id.phone_number_value)
        accountPhoneNumber.text = FirebaseAuth.getInstance().currentUser?.phoneNumber
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