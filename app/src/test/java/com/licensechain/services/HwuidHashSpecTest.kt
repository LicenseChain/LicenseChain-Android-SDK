package com.licensechain.services

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HwuidHashSpecTest {
    @Test
    fun defaultHwuidMatchesCanonicalHashSpec() {
        val service = LicenseService(
            apiKey = "test-api-key",
            baseUrl = "https://api.licensechain.app"
        )

        val method = service::class.java.getDeclaredMethod("defaultHwuid")
        method.isAccessible = true

        val h1 = method.invoke(service) as String
        val h2 = method.invoke(service) as String

        assertEquals(h1, h2)
        assertTrue(h1.matches(Regex("^[a-f0-9]{64}$")))
    }
}
