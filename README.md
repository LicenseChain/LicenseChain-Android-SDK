# LicenseChain Android SDK

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Android](https://img.shields.io/badge/Android-API%2021+-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8+-purple.svg)](https://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-7.0+-green.svg)](https://gradle.org/)

Official Android SDK for LicenseChain - Secure license management for Android applications.

## 🚀 Features

- **🔐 Secure Authentication** - User registration, login, and session management
- **📜 License Management** - Create, validate, update, and revoke licenses
- **🛡️ Hardware ID Validation** - Prevent license sharing and unauthorized access
- **🔔 Webhook Support** - Real-time license events and notifications
- **📊 Analytics Integration** - Track license usage and performance metrics
- **⚡ High Performance** - Optimized for Android's runtime
- **🔄 Async Operations** - Non-blocking HTTP requests and data processing
- **🛠️ Easy Integration** - Simple API with comprehensive documentation

## 📦 Installation

### Method 1: Gradle (Recommended)

Add to your `build.gradle` (Module: app):

```gradle
dependencies {
    implementation 'com.licensechain:licensechain-android-sdk:1.0.0'
}
```

### Method 2: Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.licensechain</groupId>
    <artifactId>licensechain-android-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Method 3: Manual Installation

1. Download the latest AAR from [GitHub Releases](https://github.com/LicenseChain/LicenseChain-Android-SDK/releases)
2. Place the AAR file in your `libs` folder
3. Add to your `build.gradle`:

```gradle
dependencies {
    implementation files('libs/licensechain-android-sdk-1.0.0.aar')
}
```

## 🚀 Quick Start

### Basic Setup

```kotlin
import com.licensechain.LicenseChainClient
import com.licensechain.LicenseChainConfig

class MainActivity : AppCompatActivity() {
    private lateinit var licenseClient: LicenseChainClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize the client
        val config = LicenseChainConfig.Builder()
            .apiKey("your-api-key")
            .appName("your-app-name")
            .version("1.0.0")
            .build()
            
        licenseClient = LicenseChainClient(config)
        
        // Connect to LicenseChain
        lifecycleScope.launch {
            try {
                licenseClient.connect()
                Log.d("LicenseChain", "Connected successfully!")
            } catch (e: Exception) {
                Log.e("LicenseChain", "Failed to connect: ${e.message}")
            }
        }
    }
}
```

### User Authentication

```kotlin
// Register a new user
lifecycleScope.launch {
    try {
        val user = licenseClient.register("username", "password", "email@example.com")
        Log.d("LicenseChain", "User registered successfully! ID: ${user.id}")
    } catch (e: Exception) {
        Log.e("LicenseChain", "Registration failed: ${e.message}")
    }
}

// Login existing user
lifecycleScope.launch {
    try {
        val user = licenseClient.login("username", "password")
        Log.d("LicenseChain", "User logged in successfully! Session ID: ${user.sessionId}")
    } catch (e: Exception) {
        Log.e("LicenseChain", "Login failed: ${e.message}")
    }
}
```

### License Management

```kotlin
// Validate a license
lifecycleScope.launch {
    try {
        val license = licenseClient.validateLicense("LICENSE-KEY-HERE")
        Log.d("LicenseChain", "License is valid!")
        Log.d("LicenseChain", "License Key: ${license.key}")
        Log.d("LicenseChain", "Status: ${license.status}")
        Log.d("LicenseChain", "Expires: ${license.expires}")
        Log.d("LicenseChain", "Features: ${license.features.joinToString(", ")}")
        Log.d("LicenseChain", "User: ${license.user}")
    } catch (e: Exception) {
        Log.e("LicenseChain", "License validation failed: ${e.message}")
    }
}

// Get user's licenses
lifecycleScope.launch {
    try {
        val licenses = licenseClient.getUserLicenses()
        Log.d("LicenseChain", "Found ${licenses.size} licenses:")
        licenses.forEachIndexed { index, license ->
            Log.d("LicenseChain", "  ${index + 1}. ${license.key} - ${license.status} (Expires: ${license.expires})")
        }
    } catch (e: Exception) {
        Log.e("LicenseChain", "Failed to get licenses: ${e.message}")
    }
}
```

### Hardware ID Validation

```kotlin
// Get hardware ID (automatically generated)
val hardwareId = licenseClient.getHardwareId()
Log.d("LicenseChain", "Hardware ID: $hardwareId")

// Validate hardware ID with license
lifecycleScope.launch {
    try {
        val isValid = licenseClient.validateHardwareId("LICENSE-KEY-HERE", hardwareId)
        if (isValid) {
            Log.d("LicenseChain", "Hardware ID is valid for this license!")
        } else {
            Log.d("LicenseChain", "Hardware ID is not valid for this license.")
        }
    } catch (e: Exception) {
        Log.e("LicenseChain", "Hardware ID validation failed: ${e.message}")
    }
}
```

### Webhook Integration

```kotlin
// Set up webhook handler
licenseClient.setWebhookHandler { event, data ->
    Log.d("LicenseChain", "Webhook received: $event")
    
    when (event) {
        "license.created" -> Log.d("LicenseChain", "New license created: ${data["licenseKey"]}")
        "license.updated" -> Log.d("LicenseChain", "License updated: ${data["licenseKey"]}")
        "license.revoked" -> Log.d("LicenseChain", "License revoked: ${data["licenseKey"]}")
    }
}

// Start webhook listener
lifecycleScope.launch {
    try {
        licenseClient.startWebhookListener()
        Log.d("LicenseChain", "Webhook listener started successfully!")
    } catch (e: Exception) {
        Log.e("LicenseChain", "Failed to start webhook listener: ${e.message}")
    }
}
```

## 📚 API Reference

### LicenseChainClient

#### Constructor

```kotlin
val config = LicenseChainConfig.Builder()
    .apiKey("your-api-key")
    .appName("your-app-name")
    .version("1.0.0")
    .baseUrl("https://api.licensechain.app") // Optional
    .build()
    
val client = LicenseChainClient(config)
```

#### Methods

##### Connection Management

```kotlin
// Connect to LicenseChain
suspend fun connect()

// Disconnect from LicenseChain
suspend fun disconnect()

// Check connection status
fun isConnected(): Boolean
```

##### User Authentication

```kotlin
// Register a new user
suspend fun register(username: String, password: String, email: String): User

// Login existing user
suspend fun login(username: String, password: String): User

// Logout current user
suspend fun logout()

// Get current user info
suspend fun getCurrentUser(): User
```

##### License Management

```kotlin
// Validate a license
suspend fun validateLicense(licenseKey: String): License

// Get user's licenses
suspend fun getUserLicenses(): List<License>

// Create a new license
suspend fun createLicense(userId: String, features: List<String>, expires: String): License

// Update a license
suspend fun updateLicense(licenseKey: String, updates: Map<String, Any>): License

// Revoke a license
suspend fun revokeLicense(licenseKey: String)

// Extend a license
suspend fun extendLicense(licenseKey: String, days: Int): License
```

##### Hardware ID Management

```kotlin
// Get hardware ID
fun getHardwareId(): String

// Validate hardware ID
suspend fun validateHardwareId(licenseKey: String, hardwareId: String): Boolean

// Bind hardware ID to license
suspend fun bindHardwareId(licenseKey: String, hardwareId: String)
```

##### Webhook Management

```kotlin
// Set webhook handler
fun setWebhookHandler(handler: (String, Map<String, String>) -> Unit)

// Start webhook listener
suspend fun startWebhookListener()

// Stop webhook listener
suspend fun stopWebhookListener()
```

##### Analytics

```kotlin
// Track event
suspend fun trackEvent(eventName: String, properties: Map<String, Any>)

// Get analytics data
suspend fun getAnalytics(timeRange: String): Analytics
```

## 🔧 Configuration

### Android Manifest

Add required permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

### ProGuard Rules

Add to your `proguard-rules.pro`:

```proguard
# LicenseChain SDK
-keep class com.licensechain.** { *; }
-keepclassmembers class com.licensechain.** { *; }
-dontwarn com.licensechain.**
```

### Environment Variables

Set these in your `build.gradle` or through your build process:

```gradle
android {
    defaultConfig {
        buildConfigField "String", "LICENSECHAIN_API_KEY", "\"your-api-key\""
        buildConfigField "String", "LICENSECHAIN_APP_NAME", "\"your-app-name\""
        buildConfigField "String", "LICENSECHAIN_APP_VERSION", "\"1.0.0\""
    }
}
```

### Advanced Configuration

```kotlin
val config = LicenseChainConfig.Builder()
    .apiKey("your-api-key")
    .appName("your-app-name")
    .version("1.0.0")
    .baseUrl("https://api.licensechain.app")
    .timeout(30)        // Request timeout in seconds
    .retries(3)         // Number of retry attempts
    .debug(false)       // Enable debug logging
    .userAgent("MyApp/1.0.0")  // Custom user agent
    .build()
```

## 🛡️ Security Features

### Hardware ID Protection

The SDK automatically generates and manages hardware IDs to prevent license sharing:

```kotlin
// Hardware ID is automatically generated and stored
val hardwareId = licenseClient.getHardwareId()

// Validate against license
val isValid = licenseClient.validateHardwareId(licenseKey, hardwareId)
```

### Secure Communication

- All API requests use HTTPS
- API keys are securely stored and transmitted
- Session tokens are automatically managed
- Webhook signatures are verified

### License Validation

- Real-time license validation
- Hardware ID binding
- Expiration checking
- Feature-based access control

## 📊 Analytics and Monitoring

### Event Tracking

```kotlin
// Track custom events
lifecycleScope.launch {
    licenseClient.trackEvent("app.started", mapOf(
        "level" to 1,
        "playerCount" to 10
    ))
}

// Track license events
lifecycleScope.launch {
    licenseClient.trackEvent("license.validated", mapOf(
        "licenseKey" to "LICENSE-KEY",
        "features" to "premium,unlimited"
    ))
}
```

### Performance Monitoring

```kotlin
// Get performance metrics
lifecycleScope.launch {
    try {
        val metrics = licenseClient.getPerformanceMetrics()
        Log.d("LicenseChain", "API Response Time: ${metrics.averageResponseTime}ms")
        Log.d("LicenseChain", "Success Rate: ${metrics.successRate * 100}%")
        Log.d("LicenseChain", "Error Count: ${metrics.errorCount}")
    } catch (e: Exception) {
        Log.e("LicenseChain", "Failed to get metrics: ${e.message}")
    }
}
```

## 🔄 Error Handling

### Custom Exception Types

```kotlin
try {
    val license = licenseClient.validateLicense("invalid-key")
} catch (e: InvalidLicenseException) {
    Log.e("LicenseChain", "License key is invalid")
} catch (e: ExpiredLicenseException) {
    Log.e("LicenseChain", "License has expired")
} catch (e: NetworkException) {
    Log.e("LicenseChain", "Network connection failed: ${e.message}")
} catch (e: LicenseChainException) {
    Log.e("LicenseChain", "LicenseChain error: ${e.message}")
}
```

### Retry Logic

```kotlin
// Automatic retry for network errors
val config = LicenseChainConfig.Builder()
    .apiKey("your-api-key")
    .appName("your-app-name")
    .version("1.0.0")
    .retries(3)        // Retry up to 3 times
    .timeout(30)       // Wait 30 seconds for each request
    .build()
```

## 🧪 Testing

### Unit Tests

```kotlin
// Example test
@Test
fun testValidateLicense() = runTest {
    val client = LicenseChainClient(testConfig)
    val license = client.validateLicense("test-license-key")
    assertTrue(license.isValid)
}
```

### Integration Tests

```kotlin
// Test with real API
@Test
fun testIntegration() = runTest {
    val client = LicenseChainClient(realConfig)
    client.connect()
    val licenses = client.getUserLicenses()
    assertNotNull(licenses)
}
```

## 📝 Examples

See the `examples/` directory for complete examples:

- `BasicUsageActivity.kt` - Basic SDK usage
- `AdvancedFeaturesActivity.kt` - Advanced features and configuration
- `WebhookIntegrationActivity.kt` - Webhook handling

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Setup

1. Clone the repository
2. Install Android Studio
3. Install Android SDK 21 or later
4. Build: `./gradlew build`
5. Test: `./gradlew test`

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [https://docs.licensechain.app/android](https://docs.licensechain.app/android)
- **Issues**: [GitHub Issues](https://github.com/LicenseChain/LicenseChain-Android-SDK/issues)
- **Discord**: [LicenseChain Discord](https://discord.gg/licensechain)
- **Email**: support@licensechain.app

## 🔗 Related Projects

- [LicenseChain iOS SDK](https://github.com/LicenseChain/LicenseChain-iOS-SDK)
- [LicenseChain Unity SDK](https://github.com/LicenseChain/LicenseChain-Unity-SDK)
- [LicenseChain Customer Panel](https://github.com/LicenseChain/LicenseChain-Customer-Panel)

---

**Made with ❤️ for the Android community**
