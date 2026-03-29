package com.licensechain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.interfaces.DecodedJWT
import java.net.URL
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit

/**
 * RS256 license_token verification via JWKS (parity with Node verifyLicenseAssertionJwt).
 */
object LicenseAssertion {
    const val LICENSE_TOKEN_USE_CLAIM = "licensechain_license_v1"

    data class VerifyOptions(
        val expectedAppId: String? = null,
        val issuer: String? = null
    )

    @JvmStatic
    fun verifyLicenseAssertionJwt(token: String, jwksUrl: String, options: VerifyOptions = VerifyOptions()): DecodedJWT {
        val trimmedToken = token.trim()
        require(trimmedToken.isNotEmpty()) { "empty token" }
        val trimmedJwks = jwksUrl.trim()
        require(trimmedJwks.isNotEmpty()) { "empty jwksUrl" }

        val decoded = JWT.decode(trimmedToken)
        val kid = decoded.keyId ?: throw SecurityException("JWT missing kid")
        val provider = JwkProviderBuilder(URL(trimmedJwks))
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
        val jwk = provider[kid]
        val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
        val builder = JWT.require(algorithm)
        if (!options.issuer.isNullOrBlank()) {
            builder.withIssuer(options.issuer.trim())
        }
        val verified = builder.build().verify(trimmedToken)

        val tu = verified.getClaim("token_use").asString()
        if (tu != LICENSE_TOKEN_USE_CLAIM) {
            throw SecurityException("Invalid license token: expected token_use \"$LICENSE_TOKEN_USE_CLAIM\"")
        }

        val wantApp = options.expectedAppId?.trim()
        if (!wantApp.isNullOrEmpty()) {
            val aud = verified.audience
            val ok = aud != null && aud.contains(wantApp)
            if (!ok) {
                throw SecurityException("Invalid license token: aud does not match expected app id")
            }
        }

        return verified
    }
}
