package com.example.mynewsapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mynewsapp.R
import com.google.firebase.auth.FirebaseAuth


class DisplayArticleActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_article)

        val b = intent.extras
        val url = b!!.getString("url")

        val webView = findViewById<WebView>(R.id.article_web_view)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        if (url != null) {
            webView.loadUrl(url)
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