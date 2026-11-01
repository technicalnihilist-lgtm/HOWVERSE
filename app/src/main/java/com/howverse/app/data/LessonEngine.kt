package com.howverse.app.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()

/**
 * Talks to the HOWVERSE AI backend when a URL is configured, and falls back to
 * the offline library when it is not — so the app always answers something.
 */
object LessonEngine {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .callTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun ask(
        question: String,
        category: String,
        backendUrl: String,
        language: String = "English"
    ): Lesson {
        val base = backendUrl.trim().trimEnd('/')
        if (base.isBlank() || !base.startsWith("http")) {
            val offline = OfflineBrain.lesson(question, category)
            return if (language.equals("English", true)) offline else offline.copy(
                notice = "The built-in offline library is English only. Add your AI backend URL " +
                    "in Profile to get answers in $language."
            )
        }
        return withContext(Dispatchers.IO) {
            try {
                val payload = JSONObject()
                    .put("question", question)
                    .put("category", category)
                    .put("language", language)
                    .put(
                        "instruction",
                        "Answer in $language. Use simple, step-by-step language and include " +
                            "practical examples. Refuse anything dangerous or illegal."
                    )
                val request = Request.Builder()
                    .url("$base/api/learn")
                    .post(payload.toString().toRequestBody(JSON_MEDIA))
                    .build()
                client.newCall(request).execute().use { response ->
                    val raw = response.body?.string().orEmpty()
                    if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
                    val json = JSONObject(raw)
                    val answer = json.optString("answer").ifBlank { "The AI returned an empty answer." }
                    val arr = json.optJSONArray("sources")
                    val sources = if (arr == null) emptyList() else (0 until arr.length()).mapNotNull { i ->
                        arr.optJSONObject(i)?.optString("title")?.takeIf { it.isNotBlank() }
                    }
                    Lesson(answer = answer, sources = sources, fromAI = true)
                }
            } catch (e: Exception) {
                val fallback = OfflineBrain.lesson(question, category)
                fallback.copy(
                    notice = "Couldn't reach your AI backend (${e.message ?: "network error"}). " +
                        "Showing the offline guide instead."
                )
            }
        }
    }
}
