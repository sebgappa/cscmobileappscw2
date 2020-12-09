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

/**
 * This activity class is used to load and display full articles from the web.
 * @author Sebastian Gappa
 */
class DisplayArticleActivity: AppCompatActivity() {

    /**
     * When the activity is created we launch the webView client with the url
     * passed from the parent activity which displays the articles.
     */
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
    }
}