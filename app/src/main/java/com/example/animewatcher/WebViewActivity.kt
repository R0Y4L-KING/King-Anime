package com.example.animewatcher

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var darkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val url = intent.getStringExtra("url") ?: "https://google.com"

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)

        findViewById<Button>(R.id.btnDark).setOnClickListener {
            darkMode = !darkMode
            val js = if (darkMode) {
                "document.documentElement.style.filter='invert(1) hue-rotate(180deg)';"
            } else {
                "document.documentElement.style.filter='none';"
            }
            webView.evaluateJavascript(js, null)
        }

        findViewById<Button>(R.id.btnRefresh).setOnClickListener {
            webView.reload()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
