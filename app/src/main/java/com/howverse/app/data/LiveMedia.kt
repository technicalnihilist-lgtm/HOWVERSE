package com.howverse.app.data

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

/**
 * Live content from Wikimedia Commons.
 *
 * When the learner opens a section, HOWVERSE asks the Commons search API for current
 * free-licensed photographs of that topic, so newly uploaded pictures appear without a new
 * build. Only CC0, public-domain, CC BY and CC BY-SA files are accepted, and every photo
 * travels with its author, licence and source page so the app can credit it properly.
 */
data class LivePhoto(
    val title: String,
    val thumbUrl: String,
    val pageUrl: String,
    val author: String,
    val license: String,
    val licenseUrl: String
)

object LiveMedia {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .callTimeout(40, TimeUnit.SECONDS)
        .build()

    /** Search terms per section id — short, concrete phrases that Commons indexes well. */
    private val queries: Map<String, String> = mapOf(
        "a" to "painting studio artist",
        "b" to "business meeting office",
        "c" to "programming code screen",
        "d" to "server room data center",
        "e" to "welding workshop sparks",
        "f" to "biryani dish",
        "g" to "mountain valley landscape",
        "h" to "temple heritage india",
        "i" to "television camera studio",
        "j" to "press conference journalist",
        "k" to "library books shelves",
        "l" to "manuscript calligraphy",
        "m" to "blackboard mathematics equations",
        "n" to "forest river landscape",
        "o" to "office desk workspace",
        "p" to "science laboratory experiment",
        "q" to "quality control laboratory",
        "r" to "industrial robot arm",
        "s" to "team workshop collaboration",
        "t" to "express train station",
        "u" to "plumber pipes tools",
        "v" to "orchestra concert stage",
        "w" to "festival celebration crowd",
        "x" to "radio telescope observatory",
        "y" to "yoga class outdoor",
        "z" to "vocational training workshop",
        "star" to "world globe antique"
    )

    private val blockedWords = listOf(
        "map", "diagram", "logo", "chart", "sign", "poster", "stamp", "flag",
        "screenshot", "seal of", "plate"
    )

    private val cache = HashMap<String, List<LivePhoto>>()
    private val bitmapCache = HashMap<String, ImageBitmap>()

    private val extensionRegex = Regex("\\.(jpg|jpeg|png|webp|tif|tiff)$", RegexOption.IGNORE_CASE)

    /** Fresh photos for a section. Returns an empty list when the network or the API fails. */
    suspend fun photos(sectionId: String, sectionTitle: String): List<LivePhoto> {
        cache[sectionId]?.let { return it }
        return withContext(Dispatchers.IO) {
            val query = queries[sectionId] ?: sectionTitle
            val request = Request.Builder()
                .url(searchUrl(query))
                .header("User-Agent", "HOWVERSE/2.0 (Android; educational)")
                .build()
            val body = client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IllegalStateException("Commons HTTP ${response.code}")
                response.body?.string().orEmpty()
            }
            val pages = JSONObject(body).optJSONObject("query")?.optJSONObject("pages")
                ?: return@withContext emptyList<LivePhoto>()

            val found = ArrayList<LivePhoto>()
            val keys = pages.keys()
            while (keys.hasNext()) {
                val page = pages.optJSONObject(keys.next()) ?: continue
                val info = page.optJSONArray("imageinfo")?.optJSONObject(0) ?: continue
                val thumb = info.optString("thumburl")
                if (thumb.isBlank()) continue
                val meta = info.optJSONObject("extmetadata") ?: JSONObject()
                val license = stripHtml(meta.optJSONObject("LicenseShortName")?.optString("value").orEmpty())
                if (!isFree(license)) continue
                val rawTitle = page.optString("title").removePrefix("File:")
                val lower = rawTitle.lowercase()
                if (blockedWords.any { lower.contains(it) }) continue
                if (info.optInt("width") < 600) continue
                found.add(
                    LivePhoto(
                        title = rawTitle.replace(extensionRegex, ""),
                        thumbUrl = thumb,
                        pageUrl = info.optString("descriptionurl").ifBlank {
                            "https://commons.wikimedia.org/wiki/" + URLEncoder.encode(page.optString("title"), "UTF-8")
                        },
                        author = stripHtml(meta.optJSONObject("Artist")?.optString("value").orEmpty())
                            .ifBlank { "Unknown author" },
                        license = license,
                        licenseUrl = stripHtml(meta.optJSONObject("LicenseUrl")?.optString("value").orEmpty())
                    )
                )
                if (found.size >= 8) break
            }
            if (found.isNotEmpty()) cache[sectionId] = found
            found
        }
    }

    /** Downloads and decodes a thumbnail, memoised for the session. */
    suspend fun bitmap(url: String): ImageBitmap? {
        bitmapCache[url]?.let { return it }
        return withContext(Dispatchers.IO) {
            val decoded = runCatching {
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) return@use null
                    val bytes = response.body?.bytes() ?: return@use null
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
                }
            }.getOrNull()
            if (decoded != null) bitmapCache[url] = decoded
            decoded
        }
    }

    private fun searchUrl(query: String): String {
        val encoded = URLEncoder.encode("$query filetype:bitmap", "UTF-8")
        return "https://commons.wikimedia.org/w/api.php?action=query&format=json" +
            "&generator=search&gsrsearch=$encoded&gsrnamespace=6&gsrlimit=10" +
            "&prop=imageinfo&iiprop=url|extmetadata|size&iiurlwidth=640"
    }

    private fun isFree(license: String): Boolean {
        val l = license.lowercase()
        return l.contains("public domain") || l.startsWith("cc0") ||
            l.startsWith("cc by") || l.startsWith("cc-by")
    }

    private fun stripHtml(value: String): String =
        value
            .replace(Regex("<[^>]*>"), "")
            .replace("&amp;", "&")
            .replace("&#39;", "'")
            .replace("&quot;", "\"")
            .trim()
}
