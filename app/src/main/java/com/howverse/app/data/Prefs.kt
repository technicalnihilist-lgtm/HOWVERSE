package com.howverse.app.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Small persistent store: theme, teaching language, AI backend URL, pack URL and
 * saved lessons. Everything survives a restart.
 */
class Prefs(context: Context) {

    private val sp = context.getSharedPreferences("howverse_prefs", Context.MODE_PRIVATE)

    var themeMode: String
        get() = sp.getString("theme_mode", "system") ?: "system"
        set(value) = sp.edit().putString("theme_mode", value).apply()

    var backendUrl: String
        get() = sp.getString("backend_url", "") ?: ""
        set(value) = sp.edit().putString("backend_url", value.trim()).apply()

    var languageCode: String
        get() = sp.getString("language_code", "en") ?: "en"
        set(value) = sp.edit().putString("language_code", value).apply()

    var packUrl: String
        get() = sp.getString("pack_url", "") ?: ""
        set(value) = sp.edit().putString("pack_url", value.trim()).apply()

    fun loadSaved(): List<SavedLesson> {
        val raw = sp.getString("saved", "[]") ?: "[]"
        return try {
            val arr = JSONArray(raw)
            (0 until arr.length()).mapNotNull { i ->
                val o = arr.optJSONObject(i) ?: return@mapNotNull null
                val question = o.optString("q")
                if (question.isBlank()) return@mapNotNull null
                SavedLesson(o.optLong("id"), question, o.optString("a"), o.optLong("t"))
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun persistSaved(items: List<SavedLesson>) {
        val arr = JSONArray()
        items.forEach {
            arr.put(
                JSONObject()
                    .put("id", it.id)
                    .put("question", it.question)
                    .put("a", it.answer)
                    .put("t", it.savedAt)
            )
        }
        sp.edit().putString("saved", arr.toString()).apply()
    }
}
