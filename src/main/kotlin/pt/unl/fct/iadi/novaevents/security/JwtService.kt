package pt.unl.fct.iadi.novaevents.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    private val environment: Environment,
) {
    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
    }

    private val jwtSecret: String
        get() = environment.getProperty(
            "app.security.jwt.secret",
            "NovaEventsJwtSecretKeyForSigningTokens2026NovaEvents",
        )

    private val expirationSeconds: Long
        get() = environment.getProperty("app.security.jwt.expiration-seconds", Long::class.java, 43200L)

    private val cookieSecure: Boolean
        get() = environment.getProperty("app.security.jwt.cookie-secure", Boolean::class.java, false)

    fun generateToken(username: String, authorities: Collection<String>): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(username)
            .claim("roles", authorities.toList())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expirationSeconds)))
            .signWith(signingKey)
            .compact()
    }

    fun parseToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun addJwtCookie(response: jakarta.servlet.http.HttpServletResponse, token: String) {
        response.addHeader(HttpHeaders.SET_COOKIE, buildJwtCookie(token).toString())
    }

    fun clearJwtCookie(response: jakarta.servlet.http.HttpServletResponse) {
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            buildJwtCookie("").maxAge(Duration.ZERO).build().toString(),
        )
    }

    private fun buildJwtCookie(token: String): ResponseCookie.ResponseCookieBuilder {
        return ResponseCookie.from(JWT_COOKIE_NAME, token)
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Lax")
            .path("/")
            .maxAge(Duration.ofSeconds(expirationSeconds))
    }

    companion object {
        const val JWT_COOKIE_NAME = "jwt"
    }
}
