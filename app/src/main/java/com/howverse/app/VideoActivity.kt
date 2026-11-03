package com.howverse.app

import android.annotation.SuppressLint
import android.content.SharedPreferences
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
 *
 * Two modes, chosen with the button in the header and remembered for next time:
 *  - Mobile  : m.youtube.com, lighter page, relevance order.
 *  - Desktop : www.youtube.com with a desktop user agent, which is the only view that honours
 *              the upload-date filter (sp=CAI%3D) — so "Latest" really shows the newest uploads.
 */
class VideoActivity : ComponentActivity() {

    private lateinit var web: WebView
    private lateinit var modeButton: TextView
    private lateinit var prefs: SharedPreferences
    private var query: String = ""
    private var sortByDate: Boolean = false
    private var desktop: Boolean = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = intent.getStringExtra(EXTRA_QUERY).orEmpty().ifBlank { "how to learn anything" }
        sortByDate = intent.getBooleanExtra(EXTRA_SORT_BY_DATE, false)
        prefs = getSharedPreferences("howverse_prefs", MODE_PRIVATE)
        // First run of the Latest button starts in desktop mode, because mobile drops the sort.
        desktop = intent.getBooleanExtra(EXTRA_DESKTOP, prefs.getBoolean(KEY_DESKTOP, sortByDate))

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#080B13"))
        }

        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(28, 40, 20, 28)
            gravity = Gravity.CENTER_VERTICAL
        }
        val title = TextView(this).apply {
            text = (if (sortByDate) "Latest · " else "Videos · ") + query
            setTextColor(Color.parseColor("#F2F5FB"))
            textSize = 15f
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        }
        modeButton = TextView(this).apply {
            setTextColor(Color.parseColor("#0B1020"))
            textSize = 12f
            setPadding(26, 12, 26, 12)
            setBackgroundColor(Color.parseColor("#9DBBFF"))
            setOnClickListener {
                desktop = !desktop
                prefs.edit().putBoolean(KEY_DESKTOP, desktop).apply()
                applyMode()
            }
        }
        val close = TextView(this).apply {
            text = "Close"
            setTextColor(Color.parseColor("#9DBBFF"))
            textSize = 14f
            setPadding(20, 12, 4, 12)
            setOnClickListener { finish() }
        }
        bar.addView(title)
        bar.addView(modeButton)
        bar.addView(close)

        web = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            setBackgroundColor(Color.parseColor("#080B13"))
        }

        val hint = TextView(this).apply {
            text = "Mobile is lighter; Desktop is the only mode where Latest really sorts by upload date."
            setTextColor(Color.parseColor("#8A97AC"))
            textSize = 11f
            setPadding(28, 0, 24, 18)
        }

        root.addView(bar)
        root.addView(hint)
        root.addView(web, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        setContentView(root)

        applyMode()
    }

    private fun applyMode() {
        modeButton.text = if (desktop) "Desktop" else "Mobile"
        // null restores the WebView's own mobile user agent.
        web.settings.userAgentString = if (desktop) DESKTOP_USER_AGENT else null

        val host = if (desktop) {
            "https://www.youtube.com/results?search_query="
        } else {
            "https://m.youtube.com/results?search_query="
        }
        // sp=CAI%3D asks for upload-date order; the desktop view honours it, mobile ignores it.
        val sort = if (sortByDate) "&sp=CAI%3D" else ""
        web.loadUrl(host + Uri.encode("$query tutorial step by step") + sort)
    }

    companion object {
        const val EXTRA_QUERY = "query"
        const val EXTRA_SORT_BY_DATE = "sortByDate"
        const val EXTRA_DESKTOP = "desktopMode"
        private const val KEY_DESKTOP = "video_desktop"

        private const val DESKTOP_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/121.0.0.0 Safari/537.36"
    }
}
