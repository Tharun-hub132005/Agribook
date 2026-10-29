package com.example.agribook.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.concurrent.ConcurrentHashMap

object SecurityService {
    private val random = SecureRandom()
    private val rateLimitTracker = ConcurrentHashMap<String, MutableList<Long>>()
    private const val MAX_ATTEMPTS = 5
    private const val WINDOW_MS = 60_000L // 1 minute window

    fun generateSalt(): String {
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return saltBytes.joinToString("") { "%02x".format(it) }
    }

    fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val combined = "$password:$salt"
        val hash = digest.digest(combined.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val hash = hashPassword(password, salt)
        return hash == expectedHash
    }

    /**
     * Rate limiter: Checks if key (phone/email/ip) has exceeded max attempts within the time window.
     * Returns true if allowed, false if rate limited.
     */
    @Synchronized
    fun checkRateLimit(key: String): Boolean {
        val now = System.currentTimeMillis()
        val list = rateLimitTracker.getOrPut(key) { mutableListOf() }
        list.removeAll { now - it > WINDOW_MS }
        if (list.size >= MAX_ATTEMPTS) {
            return false
        }
        list.add(now)
        return true
    }

    fun resetRateLimit(key: String) {
        rateLimitTracker.remove(key)
    }

    fun validatePasswordStrength(password: String): Pair<Boolean, String> {
        if (password.length < 6) {
            return Pair(false, "Password must be at least 6 characters long")
        }
        return Pair(true, "")
    }

    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace(Regex("(?is)<script.*?>.*?</script>"), "")
            .replace(Regex("(?is)<style.*?>.*?</style>"), "")
            .replace(Regex("<.*?>"), "")
            .trim()
    }
}
