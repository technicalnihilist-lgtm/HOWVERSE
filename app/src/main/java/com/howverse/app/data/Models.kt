package com.howverse.app.data

/** One answer produced by the AI backend, or by the offline library as a fallback. */
data class Lesson(
    val answer: String,
    val sources: List<String> = emptyList(),
    val fromAI: Boolean = false,
    val notice: String = ""
)

data class SavedLesson(
    val id: Long,
    val question: String,
    val answer: String,
    val savedAt: Long
)

enum class Role { USER, AI }

data class ChatMessage(
    val id: Long,
    val role: Role,
    val text: String,
    val sources: List<String> = emptyList(),
    val fromAI: Boolean = false,
    val pending: Boolean = false,
    val notice: String = ""
)
