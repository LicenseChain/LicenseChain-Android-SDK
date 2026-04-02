# JWKS-only license assertion (Kotlin)

Use this when you already have a **`license_token`** and **`license_jwks_uri`** (or you standardize on the public JWKS URL) and want to verify **without** calling `verify` again in the same flow.

- JWKS: `https://api.licensechain.app/v1/licenses/jwks` (or the `license_jwks_uri` returned with the token).
- Claim **`token_use`** must be **`licensechain_license_v1`** (see `LicenseAssertion.LICENSE_TOKEN_USE_CLAIM`).

```kotlin
import com.licensechain.LicenseAssertion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun verifyJwtOnly(token: String, jwksUrl: String) = withContext(Dispatchers.IO) {
    LicenseAssertion.verifyLicenseAssertionJwt(
        token,
        jwksUrl,
        LicenseAssertion.VerifyOptions(
            expectedAppId = null, // set to your Dashboard app id to enforce `aud`
            issuer = "https://api.licensechain.app" // optional; match Core API LICENSE_JWT_ISSUER
        )
    )
}
```

Pass a **real** token from a successful verify response (or your BYOK-issued flow). For the full verify-then-assert path, see [LICENSE_ASSERTION_JWKS.md](./LICENSE_ASSERTION_JWKS.md).
