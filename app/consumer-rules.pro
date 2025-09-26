# Consumer ProGuard rules for LicenseChain Android SDK
# These rules will be applied to apps that use this library

# LicenseChain SDK
-keep class com.licensechain.** { *; }
-keepclassmembers class com.licensechain.** { *; }
-dontwarn com.licensechain.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
