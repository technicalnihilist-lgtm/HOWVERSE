package com.howverse.app.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Everything the app writes to disk lives here: lessons and whole sections saved as
 * plain text files the user can keep, and topic packs pulled from a catalogue URL.
 */
object Downloader {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .callTimeout(90, TimeUnit.SECONDS)
        .build()

    fun safeName(raw: String): String {
        val cleaned = raw.replace(Regex("[^A-Za-z0-9 _-]"), " ").trim().replace(Regex("\\s+"), "-")
        return cleaned.ifBlank { "howverse" }.take(70)
    }

    /**
     * Saves text into the device Downloads folder on Android 10+, and into the app's
     * external downloads folder on older versions. Returns a human readable location.
     */
    fun saveText(context: Context, baseName: String, text: String): String {
        val fileName = safeName(baseName) + ".txt"
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/HOWVERSE")
                }
                val uri: Uri? = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri == null) return "Could not create the file"
                context.contentResolver.openOutputStream(uri)?.use { it.write(text.toByteArray()) }
                "Saved to Downloads/HOWVERSE/$fileName"
            } else {
                val dir = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir,
                    "HOWVERSE"
                )
                dir.mkdirs()
                val file = File(dir, fileName)
                file.writeText(text)
                "Saved to ${file.absolutePath}"
            }
        } catch (e: Exception) {
            "Could not save: ${e.message ?: "unknown error"}"
        }
    }

    private fun packsDir(context: Context): File =
        File(context.filesDir, "packs").apply { mkdirs() }

    suspend fun downloadPack(context: Context, url: String): File = withContext(Dispatchers.IO) {
        val clean = url.trim()
        require(clean.startsWith("http")) { "Enter a full https:// URL" }
        val request = Request.Builder().url(clean).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) error("HTTP ${response.code}")
            val bytes = response.body?.bytes() ?: error("Empty response")
            val nameFromUrl = clean.substringAfterLast('/').substringBefore('?')
            val name = safeName(nameFromUrl.ifBlank { "pack" }) + ".json"
            val file = File(packsDir(context), name)
            file.writeBytes(bytes)
            file
        }
    }

    fun listPacks(context: Context): List<File> =
        packsDir(context).listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun deletePack(file: File): Boolean = runCatching { file.delete() }.getOrDefault(false)
}
