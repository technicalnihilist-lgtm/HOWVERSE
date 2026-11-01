package com.howverse.app.data

/** Languages the app can teach in: the AI request language and the read-aloud voice. */
data class Language(val code: String, val label: String, val tag: String)

object Languages {
    val all: List<Language> = listOf(
        Language("en", "English", "en-IN"),
        Language("hi", "Hindi", "hi-IN"),
        Language("bn", "Bengali", "bn-IN"),
        Language("ta", "Tamil", "ta-IN"),
        Language("te", "Telugu", "te-IN"),
        Language("mr", "Marathi", "mr-IN"),
        Language("gu", "Gujarati", "gu-IN"),
        Language("pa", "Punjabi", "pa-IN"),
        Language("ur", "Urdu", "ur-IN"),
        Language("kn", "Kannada", "kn-IN"),
        Language("ml", "Malayalam", "ml-IN"),
        Language("or", "Odia", "or-IN"),
        Language("as", "Assamese", "as-IN"),
        Language("sa", "Sanskrit", "sa-IN"),
        Language("ne", "Nepali", "ne-NP"),
        Language("si", "Sinhala", "si-LK"),
        Language("ar", "Arabic", "ar-SA"),
        Language("fa", "Persian", "fa-IR"),
        Language("tr", "Turkish", "tr-TR"),
        Language("he", "Hebrew", "he-IL"),
        Language("ru", "Russian", "ru-RU"),
        Language("uk", "Ukrainian", "uk-UA"),
        Language("el", "Greek", "el-GR"),
        Language("de", "German", "de-DE"),
        Language("fr", "French", "fr-FR"),
        Language("es", "Spanish", "es-ES"),
        Language("pt", "Portuguese", "pt-PT"),
        Language("it", "Italian", "it-IT"),
        Language("nl", "Dutch", "nl-NL"),
        Language("pl", "Polish", "pl-PL"),
        Language("sv", "Swedish", "sv-SE"),
        Language("da", "Danish", "da-DK"),
        Language("fi", "Finnish", "fi-FI"),
        Language("cs", "Czech", "cs-CZ"),
        Language("ro", "Romanian", "ro-RO"),
        Language("hu", "Hungarian", "hu-HU"),
        Language("id", "Indonesian", "id-ID"),
        Language("ms", "Malay", "ms-MY"),
        Language("vi", "Vietnamese", "vi-VN"),
        Language("th", "Thai", "th-TH"),
        Language("fil", "Filipino", "fil-PH"),
        Language("sw", "Swahili", "sw-KE"),
        Language("af", "Afrikaans", "af-ZA"),
        Language("zh", "Chinese", "zh-CN"),
        Language("ja", "Japanese", "ja-JP"),
        Language("ko", "Korean", "ko-KR"),
        Language("hinglish", "Hinglish", "hi-IN")
    )

    val default: Language = all.first()

    fun byCode(code: String): Language = all.firstOrNull { it.code == code } ?: default
}
