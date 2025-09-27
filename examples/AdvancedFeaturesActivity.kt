package com.example.licensechain

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.licensechain.LicenseChainClient
import com.licensechain.LicenseChainConfig
import kotlinx.coroutines.launch

/**
 * LicenseChain Android SDK - Advanced Features Example
 * 
 * This example demonstrates advanced features of the LicenseChain Android SDK
 * including webhook integration, analytics, and error handling.
 */
class AdvancedFeaturesActivity : AppCompatActivity() {
    
    private lateinit var licenseClient: LicenseChainClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced)
        
        // Initialize the LicenseChain client with advanced configuration
        initializeLicenseChain()
        
        // Demonstrate advanced functionality
        demonstrateAdvancedFeatures()
    }
    
    private fun initializeLicenseChain() {
        Log.d("LicenseChain", "🚀 LicenseChain Android SDK - Advanced Features Example")
        Log.d("LicenseChain", "=" + "=".repeat(50))
        
        // Initialize the client with advanced configuration
        val config = LicenseChainConfig.Builder()
            .apiKey("your-api-key-here")
            .appName("MyAdvancedAndroidApp")
            .version("1.0.0")
            .baseUrl("https://api.licensechain.app")
            .timeout(30)
            .retries(3)
            .debug(true)
            .userAgent("MyApp/1.0.0")
            .build()
            
        licenseClient = LicenseChainClient(config)
        
        Log.d("LicenseChain", "✅ Advanced LicenseChain client initialized")
    }
    
    private fun demonstrateAdvancedFeatures() {
        lifecycleScope.launch {
            try {
                // Connect to LicenseChain
                Log.d("LicenseChain", "\n🔌 Connecting to LicenseChain...")
                licenseClient.connect()
                Log.d("LicenseChain", "✅ Connected to LicenseChain successfully!")
                
                // Example 1: Webhook Integration
                Log.d("LicenseChain", "\n🔔 Setting up webhook integration...")
                licenseClient.setWebhookHandler { event, data ->
                    Log.d("LicenseChain", "Webhook received: $event")
                    when (event) {
                        "license.created" -> Log.d("LicenseChain", "New license created: ${data["licenseKey"]}")
                        "license.updated" -> Log.d("LicenseChain", "License updated: ${data["licenseKey"]}")
                        "license.revoked" -> Log.d("LicenseChain", "License revoked: ${data["licenseKey"]}")
                    }
                }
                
                // Start webhook listener
                licenseClient.startWebhookListener()
                Log.d("LicenseChain", "✅ Webhook listener started!")
                
                // Example 2: Advanced Analytics
                Log.d("LicenseChain", "\n📊 Advanced analytics tracking...")
                try {
                    licenseClient.trackEvent("app.advanced_features_started", mapOf(
                        "feature" to "webhook_integration",
                        "platform" to "android",
                        "version" to "1.0.0",
                        "userAgent" to "MyApp/1.0.0"
                    ))
                    Log.d("LicenseChain", "✅ Advanced analytics tracked!")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ Failed to track analytics: ${e.message}")
                }
                
                // Example 3: Performance Monitoring
                Log.d("LicenseChain", "\n📈 Performance monitoring...")
                try {
                    val metrics = licenseClient.getPerformanceMetrics()
                    Log.d("LicenseChain", "API Response Time: ${metrics.averageResponseTime}ms")
                    Log.d("LicenseChain", "Success Rate: ${metrics.successRate * 100}%")
                    Log.d("LicenseChain", "Error Count: ${metrics.errorCount}")
                    Log.d("LicenseChain", "✅ Performance metrics retrieved!")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ Failed to get performance metrics: ${e.message}")
                }
                
                // Example 4: Error Handling
                Log.d("LicenseChain", "\n🛡️ Advanced error handling...")
                try {
                    val license = licenseClient.validateLicense("invalid-key")
                    Log.d("LicenseChain", "License validation result: ${license.key}")
                } catch (e: com.licensechain.InvalidLicenseException) {
                    Log.d("LicenseChain", "✅ Caught InvalidLicenseException: ${e.message}")
                } catch (e: com.licensechain.ExpiredLicenseException) {
                    Log.d("LicenseChain", "✅ Caught ExpiredLicenseException: ${e.message}")
                } catch (e: com.licensechain.NetworkException) {
                    Log.d("LicenseChain", "✅ Caught NetworkException: ${e.message}")
                } catch (e: Exception) {
                    Log.w("LicenseChain", "❌ Unexpected error: ${e.message}")
                }
                
                // Cleanup
                Log.d("LicenseChain", "\n🧹 Cleaning up...")
                licenseClient.stopWebhookListener()
                licenseClient.logout()
                licenseClient.disconnect()
                Log.d("LicenseChain", "✅ Cleanup completed!")
                
                Log.d("LicenseChain", "\n🎉 All advanced examples completed!")
                
            } catch (e: Exception) {
                Log.e("LicenseChain", "❌ Error during advanced demonstration: ${e.message}")
            }
        }
    }
}
