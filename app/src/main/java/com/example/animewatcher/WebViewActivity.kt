package com.example.animewatcher

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var darkMode = false
    private var originalHost: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val url = intent.getStringExtra("url") ?: "https://google.com"
        originalHost = Uri.parse(url).host ?: ""

        webView = findViewById(R.id.webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.settings.setSupportMultipleWindows(false)
        webView.settings.javaScriptCanOpenWindowsAutomatically = false

        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: android.os.Message?
            ): Boolean {
                return false
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val requestedUrl = request?.url ?: return false
                val requestedHost = requestedUrl.host ?: ""

                val isSameDomain = requestedHost == originalHost ||
                        requestedHost.endsWith(".$originalHost")

                return if (isSameDomain) {
                    false
                } else {
                    true
                }
            }
        }

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
