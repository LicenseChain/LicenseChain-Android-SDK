# License assertion JWT (RS256 + JWKS) — Android example

**JWKS-only** (token + URI you already have, no `verify` in the same flow): [JWKS_ONLY.md](./JWKS_ONLY.md).

When Core API has **`LICENSE_JWT_PRIVATE_KEY`** / **`LICENSE_JWT_PUBLIC_KEY`** configured, `POST /v1/licenses/verify` may return **`license_token`**, **`license_token_expires_at`**, and **`license_jwks_uri`**. Verify offline with **`LicenseAssertion.verifyLicenseAssertionJwt`** (same rules as other SDKs).

Use your existing **`LicenseService`** (suspend) from a coroutine:

```kotlin
import com.licensechain.LicenseAssertion
import com.licensechain.services.LicenseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun verifyAndAssertJwt(
    licenseService: LicenseService,
    licenseKey: String,
    hwuid: String? = null
) = withContext(Dispatchers.IO) {
    runCatching {
        val body = licenseService.verifyLicenseWithDetails(licenseKey, hwuid)
        if (body.get("valid")?.asBoolean != true) {
            error("license not valid")
        }
        val token = body.get("license_token")?.asString ?: return@runCatching
        val jwks = body.get("license_jwks_uri")?.asString ?: return@runCatching
        val decoded = LicenseAssertion.verifyLicenseAssertionJwt(
            token,
            jwks,
            LicenseAssertion.VerifyOptions(
                expectedAppId = null, // set to your dashboard app id to enforce `aud`
                issuer = "https://api.licensechain.app" // optional; match Core LICENSE_JWT_ISSUER
            )
        )
        // decoded.subject == license id; inspect claims as needed
        decoded
    }
}
```

Requirements: dependencies already pulled for **`LicenseAssertion`** (`java-jwt`, `jwks-rsa`). Use a **real** API key and license key from your Dashboard app.

If you only need to verify an existing JWT + JWKS URL (no `verify` in the same flow), see **[JWKS_ONLY.md](./JWKS_ONLY.md)**.
