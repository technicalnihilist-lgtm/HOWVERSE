package com.howverse.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

/**
 * In-app video browser: opens a YouTube search for the topic the user is learning.
 * Videos play in the YouTube app or player; nothing is bundled or scraped.
 */
class VideoActivity : ComponentActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val query = intent.getStringExtra(EXTRA_QUERY).orEmpty().ifBlank { "how to learn anything" }
        val sortByDate = intent.getBooleanExtra(EXTRA_SORT_BY_DATE, false)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#080B13"))
        }

        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(28, 40, 28, 28)
            gravity = Gravity.CENTER_VERTICAL
        }
        val title = TextView(this).apply {
            text = "Videos · $query"
            setTextColor(Color.parseColor("#F2F5FB"))
            textSize = 15f
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        val close = TextView(this).apply {
            text = "Close"
            setTextColor(Color.parseColor("#9DBBFF"))
            textSize = 14f
            setPadding(24, 12, 8, 12)
            setOnClickListener { finish() }
        }
        bar.addView(title)
        bar.addView(close)

        val web = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            setBackgroundColor(Color.parseColor("#080B13"))
        }
        // sp=CAI%3D asks YouTube to sort results by upload date, so the newest videos come first.
        val url = "https://m.youtube.com/results?search_query=" +
            Uri.encode("$query tutorial step by step") + (if (sortByDate) "&sp=CAI%3D" else "")
        web.loadUrl(url)

        root.addView(bar)
        root.addView(web, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)
    }

    companion object {
        const val EXTRA_QUERY = "query"
        const val EXTRA_SORT_BY_DATE = "sortByDate"
    }
}
