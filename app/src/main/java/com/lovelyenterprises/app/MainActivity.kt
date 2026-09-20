package com.lovelyenterprises.app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var offlineView: android.view.View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        offlineView = findViewById(R.id.offlineView)

        setupWebView()
        setupSwipeRefresh()
        setupOfflineRetry()
        setupFabs()

        loadSiteOrOffline()
    }

    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // Open tel:, WhatsApp (wa.me) and mailto links natively instead of inside the WebView
                return if (url.startsWith("tel:") || url.startsWith("mailto:") ||
                    url.contains("wa.me") || url.contains("api.whatsapp.com")
                ) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } catch (_: Exception) { /* ignore if no app can handle it */ }
                    true
                } else {
                    false // let WebView load normal site pages
                }
            }

            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            if (isOnline()) {
                webView.reload()
            } else {
                swipeRefresh.isRefreshing = false
                showOffline()
            }
        }
    }

    private fun setupOfflineRetry() {
        findViewById<android.widget.Button>(R.id.retryButton).setOnClickListener {
            loadSiteOrOffline()
        }
    }

    private fun setupFabs() {
        findViewById<FloatingActionButton>(R.id.fabCall).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.phone_primary)))
            startActivity(intent)
        }
        findViewById<FloatingActionButton>(R.id.fabWhatsapp).setOnClickListener {
            val url = "https://wa.me/" + getString(R.string.whatsapp_number)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun loadSiteOrOffline() {
        if (isOnline()) {
            offlineView.visibility = android.view.View.GONE
            swipeRefresh.visibility = android.view.View.VISIBLE
            webView.loadUrl(getString(R.string.website_url))
        } else {
            showOffline()
        }
    }

    private fun showOffline() {
        offlineView.visibility = android.view.View.VISIBLE
        swipeRefresh.visibility = android.view.View.GONE
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
