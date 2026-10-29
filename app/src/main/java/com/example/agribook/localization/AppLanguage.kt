package com.example.agribook.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    TELUGU("te", "Telugu", "తెలుగు"),
    HINDI("hi", "Hindi", "हिन्दी"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    TAMIL("ta", "Tamil", "தமிழ்"),
    MALAYALAM("ml", "Malayalam", "മലയാളം");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
