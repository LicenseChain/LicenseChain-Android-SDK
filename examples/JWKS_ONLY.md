# JWKS-only (`license_token`) — Kotlin

Verify a Core API **`license_token`** using **`GET /v1/licenses/jwks`** (RS256) when you **already** have the JWT string and JWKS URL (for example from a previous `verify` response stored in your app, or from CI secrets). This path does **not** call `verifyLicenseWithDetails` in the same flow.

**Full flow** (verify → then assert): see [LICENSE_ASSERTION_JWKS.md](./LICENSE_ASSERTION_JWKS.md).

JWKS URL: `https://api.licensechain.app/v1/licenses/jwks` or the **`license_jwks_uri`** returned with the token.

## Env parity (Go / Rust / PHP / Java / C#)

Use the same names as other SDK `jwks_only` samples ([JWKS_EXAMPLE_PRIORITY](https://github.com/LicenseChain/sdks/blob/main/docs/JWKS_EXAMPLE_PRIORITY.md)):

| Variable | Required | Notes |
|----------|----------|--------|
| `LICENSECHAIN_LICENSE_TOKEN` | yes | JWT from `license_token` |
| `LICENSECHAIN_LICENSE_JWKS_URI` | yes | e.g. `https://api.licensechain.app/v1/licenses/jwks` |
| `LICENSECHAIN_EXPECTED_APP_ID` | no | If set, `aud` must include this Dashboard app UUID |

On Android, **process environment** is usually empty for apps. Supply values from:

- **Secure storage** (recommended) after a successful verify, or  
- **`BuildConfig`** fields populated from `gradle.properties` / CI for internal builds, or  
- **JVM unit tests** (`test` source set), where `System.getenv()` works if Gradle passes `environment` in `testOptions`.

## Kotlin (library code)

Use **`LicenseAssertion.verifyLicenseAssertionJwt`** (`java-jwt` + `jwks-rsa` are already SDK dependencies):

```kotlin
import com.licensechain.LicenseAssertion

fun verifyJwksOnly(
    licenseToken: String,
    jwksUri: String,
    expectedAppId: String? = null,
    issuer: String? = null
) = LicenseAssertion.verifyLicenseAssertionJwt(
    licenseToken.trim(),
    jwksUri.trim(),
    LicenseAssertion.VerifyOptions(
        expectedAppId = expectedAppId?.takeIf { it.isNotBlank() },
        issuer = issuer?.takeIf { it.isNotBlank() }
    )
)
```

Optional **`Dispatchers.IO`** wrapper:

```kotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun verifyJwtOnlyIo(token: String, jwksUrl: String, expectedAppId: String? = null) =
    withContext(Dispatchers.IO) {
        verifyJwksOnly(token, jwksUrl, expectedAppId = expectedAppId, issuer = null)
    }
```

- Claim **`token_use`** must be **`licensechain_license_v1`** (see `LicenseAssertion.LICENSE_TOKEN_USE_CLAIM`).

## JVM unit test (optional `System.getenv`)

If you wire Gradle to pass env vars to the test runner, you can mirror other repos:

```kotlin
val token = System.getenv("LICENSECHAIN_LICENSE_TOKEN")?.trim().orEmpty()
val jwks = System.getenv("LICENSECHAIN_LICENSE_JWKS_URI")?.trim().orEmpty()
require(token.isNotEmpty() && jwks.isNotEmpty()) {
    "Set LICENSECHAIN_LICENSE_TOKEN and LICENSECHAIN_LICENSE_JWKS_URI"
}
val appId = System.getenv("LICENSECHAIN_EXPECTED_APP_ID")?.trim()?.takeIf { it.isNotEmpty() }
val decoded = LicenseAssertion.verifyLicenseAssertionJwt(
    token,
    jwks,
    LicenseAssertion.VerifyOptions(expectedAppId = appId, issuer = null)
)
```

Pass a **real** token from a successful verify response (or your BYOK-issued flow).
