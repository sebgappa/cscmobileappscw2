package com.example.mynewsapp

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class DisplayArticleActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_article)

        val b = intent.extras
        val url = b!!.getString("url")

        val webView = findViewById<WebView>(R.id.article_web_view)
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}