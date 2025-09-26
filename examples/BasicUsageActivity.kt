package com.example.licensechain

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.licensechain.LicenseChainClient
import com.licensechain.LicenseChainConfig
import kotlinx.coroutines.launch

/**
 * LicenseChain Android SDK - Basic Usage Example
 * 
 * This example demonstrates basic usage of the LicenseChain Android SDK
 * including initialization, user authentication, and license management.
 */
class BasicUsageActivity : AppCompatActivity() {
    
    private lateinit var licenseClient: LicenseChainClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize the LicenseChain client
        initializeLicenseChain()
        
        // Demonstrate basic functionality
        demonstrateBasicUsage()
    }
    
    private fun initializeLicenseChain() {
        Log.d("LicenseChain", "🚀 LicenseChain Android SDK - Basic Usage Example")
        Log.d("LicenseChain", "=" + "=".repeat(50))
        
        // Initialize the client
        val config = LicenseChainConfig.Builder()
            .apiKey("your-api-key-here")
            .appName("MyAndroidApp")
            .version("1.0.0")
            .debug(true)
            .build()
            
        licenseClient = LicenseChainClient(config)
        
        Log.d("LicenseChain", "✅ LicenseChain client initialized")
    }
    
    private fun demonstrateBasicUsage() {
        lifecycleScope.launch {
            try {
                // Connect to LicenseChain
                Log.d("LicenseChain", "\n🔌 Connecting to LicenseChain...")
                licenseClient.connect()
                Log.d("LicenseChain", "✅ Connected to LicenseChain successfully!")
                
                // Example 1: User Registration
                Log.d("LicenseChain", "\n📝 Registering new user...")
                try {
                    val user = licenseClient.register("testuser", "password123", "test@example.com")
                    Log.d("LicenseChain", "✅ User registered successfully!")
                    Log.d("LicenseChain", "User ID: ${user.id}")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ Registration failed: ${e.message}")
                }
                
                // Example 2: User Login
                Log.d("LicenseChain", "\n🔐 Logging in user...")
                try {
                    val user = licenseClient.login("testuser", "password123")
                    Log.d("LicenseChain", "✅ User logged in successfully!")
                    Log.d("LicenseChain", "Session ID: ${user.sessionId}")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ Login failed: ${e.message}")
                }
                
                // Example 3: License Validation
                Log.d("LicenseChain", "\n🔍 Validating license...")
                try {
                    val license = licenseClient.validateLicense("LICENSE-KEY-HERE")
                    Log.d("LicenseChain", "✅ License is valid!")
                    Log.d("LicenseChain", "License Key: ${license.key}")
                    Log.d("LicenseChain", "Status: ${license.status}")
                    Log.d("LicenseChain", "Expires: ${license.expires}")
                    Log.d("LicenseChain", "Features: ${license.features.joinToString(", ")}")
                    Log.d("LicenseChain", "User: ${license.user}")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ License validation failed: ${e.message}")
                }
                
                // Example 4: Hardware ID
                Log.d("LicenseChain", "\n🖥️ Getting hardware ID...")
                val hardwareId = licenseClient.getHardwareId()
                Log.d("LicenseChain", "Hardware ID: $hardwareId")
                
                // Example 5: Analytics
                Log.d("LicenseChain", "\n📊 Tracking analytics...")
                try {
                    licenseClient.trackEvent("app.started", mapOf(
                        "level" to 1,
                        "playerCount" to 10
                    ))
                    Log.d("LicenseChain", "✅ Event tracked successfully!")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ Failed to track event: ${e.message}")
                }
                
                // Cleanup
                Log.d("LicenseChain", "\n🧹 Cleaning up...")
                licenseClient.logout()
                licenseClient.disconnect()
                Log.d("LicenseChain", "✅ Cleanup completed!")
                
                Log.d("LicenseChain", "\n🎉 All examples completed!")
                
            } catch (e: Exception) {
                Log.e("LicenseChain", "❌ Error during demonstration: ${e.message}")
            }
        }
    }
}
