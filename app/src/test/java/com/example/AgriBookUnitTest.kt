package com.example

import com.example.agribook.localization.AgriStrings
import com.example.agribook.localization.AppLanguage
import com.example.agribook.security.SecurityService
import org.junit.Assert.*
import org.junit.Test

class AgriBookUnitTest {

    @Test
    fun testSecurityService_passwordHashingAndVerification() {
        val plain = "SecretKisanPass123"
        val salt = SecurityService.generateSalt()
        assertTrue("Salt should be 32 characters hex", salt.length == 32)

        val hash = SecurityService.hashPassword(plain, salt)
        assertNotNull(hash)
        assertTrue(hash.isNotEmpty())

        val verifySuccess = SecurityService.verifyPassword(plain, salt, hash)
        assertTrue("Password verification should succeed for matching password", verifySuccess)

        val verifyFail = SecurityService.verifyPassword("WrongPassword", salt, hash)
        assertFalse("Password verification should fail for mismatching password", verifyFail)
    }

    @Test
    fun testSecurityService_rateLimiter() {
        val testKey = "test_farmer_${System.currentTimeMillis()}"
        SecurityService.resetRateLimit(testKey)

        // First 5 attempts should pass
        for (i in 1..5) {
            val allowed = SecurityService.checkRateLimit(testKey)
            assertTrue("Attempt $i should be permitted", allowed)
        }

        // 6th attempt within window should be throttled
        val throttled = SecurityService.checkRateLimit(testKey)
        assertFalse("Attempt 6 should be blocked by rate limiter", throttled)

        SecurityService.resetRateLimit(testKey)
        assertTrue("After reset, attempt should be allowed", SecurityService.checkRateLimit(testKey))
    }

    @Test
    fun testSecurityService_inputSanitizer() {
        val dirty = "   <script>alert('hack')</script> Organic Rice Farming <b>SRI</b>  "
        val clean = SecurityService.sanitizeInput(dirty)
        assertEquals("Organic Rice Farming SRI", clean)
    }

    @Test
    fun testLocalization_allSixLanguagesSupported() {
        val supported = listOf(
            AppLanguage.ENGLISH,
            AppLanguage.TELUGU,
            AppLanguage.HINDI,
            AppLanguage.KANNADA,
            AppLanguage.TAMIL,
            AppLanguage.MALAYALAM
        )

        for (lang in supported) {
            val appName = AgriStrings.get("app_name", lang)
            val feedName = AgriStrings.get("feed", lang)
            val loginText = AgriStrings.get("login", lang)

            assertNotNull(appName)
            assertTrue("App name in ${lang.name} should not be empty", appName.isNotEmpty())
            assertTrue("Feed text in ${lang.name} should not be empty", feedName.isNotEmpty())
            assertTrue("Login text in ${lang.name} should not be empty", loginText.isNotEmpty())
        }
    }

    @Test
    fun testAppLanguage_fromCodeFallback() {
        assertEquals(AppLanguage.TELUGU, AppLanguage.fromCode("te"))
        assertEquals(AppLanguage.HINDI, AppLanguage.fromCode("hi"))
        assertEquals(AppLanguage.KANNADA, AppLanguage.fromCode("kn"))
        assertEquals(AppLanguage.TAMIL, AppLanguage.fromCode("ta"))
        assertEquals(AppLanguage.MALAYALAM, AppLanguage.fromCode("ml"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("unknown_code"))
    }

    @Test
    fun testProfileAndCropLocalization() {
        val supported = listOf(
            AppLanguage.ENGLISH,
            AppLanguage.TELUGU,
            AppLanguage.HINDI,
            AppLanguage.KANNADA,
            AppLanguage.TAMIL,
            AppLanguage.MALAYALAM
        )

        for (lang in supported) {
            val editProfile = AgriStrings.get("edit_profile", lang)
            val myCrops = AgriStrings.get("my_crops", lang)
            val addCrop = AgriStrings.get("add_crop", lang)
            val nearbyFarmers = AgriStrings.get("nearby_farmers", lang)

            assertNotNull(editProfile)
            assertTrue("edit_profile in ${lang.name} must not be empty", editProfile.isNotEmpty())
            assertTrue("my_crops in ${lang.name} must not be empty", myCrops.isNotEmpty())
            assertTrue("add_crop in ${lang.name} must not be empty", addCrop.isNotEmpty())
            assertTrue("nearby_farmers in ${lang.name} must not be empty", nearbyFarmers.isNotEmpty())
        }
    }

    @Test
    fun testDistanceCalculation_nearbyAccuracy() {
        // Tenali (16.24, 80.64) to Chebrolu (16.20, 80.53) is approx 12-13 km
        val r = 6371.0
        val lat1 = 16.24
        val lon1 = 80.64
        val lat2 = 16.20
        val lon2 = 80.53

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distKm = Math.round(r * c * 10.0) / 10.0

        assertTrue("Distance should be between 10 and 15 km", distKm in 10.0..15.0)
    }

    @Test
    fun testShareLocalization_allSixLanguagesSupported() {
        val supported = listOf(
            AppLanguage.ENGLISH,
            AppLanguage.TELUGU,
            AppLanguage.HINDI,
            AppLanguage.KANNADA,
            AppLanguage.TAMIL,
            AppLanguage.MALAYALAM
        )

        for (lang in supported) {
            val shareApp = AgriStrings.get("share_app", lang)
            val sharePost = AgriStrings.get("share_post", lang)
            val shareProfile = AgriStrings.get("share_profile", lang)
            val copyLink = AgriStrings.get("copy_link", lang)
            val linkCopied = AgriStrings.get("link_copied", lang)
            val inviteFriends = AgriStrings.get("invite_friends", lang)

            assertNotNull(shareApp)
            assertTrue("share_app in ${lang.name} must not be empty", shareApp.isNotEmpty())
            assertTrue("share_post in ${lang.name} must not be empty", sharePost.isNotEmpty())
            assertTrue("share_profile in ${lang.name} must not be empty", shareProfile.isNotEmpty())
            assertTrue("copy_link in ${lang.name} must not be empty", copyLink.isNotEmpty())
            assertTrue("link_copied in ${lang.name} must not be empty", linkCopied.isNotEmpty())
            assertTrue("invite_friends in ${lang.name} must not be empty", inviteFriends.isNotEmpty())
        }
    }
}
