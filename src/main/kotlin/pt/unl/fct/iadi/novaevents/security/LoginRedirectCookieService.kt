package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Duration

@Service
class LoginRedirectCookieService(
    @Value("\${app.security.jwt.cookie-secure:false}") private val cookieSecure: Boolean,
) {
    fun saveTarget(request: HttpServletRequest, response: HttpServletResponse) {
        if (request.method != "GET") {
            return
        }

        val target = buildTarget(request)
        if (!isSafeTarget(target)) {
            return
        }

        response.addHeader(HttpHeaders.SET_COOKIE, buildCookie(target).toString())
    }

    fun resolveTarget(request: HttpServletRequest): String? {
        val cookieValue = request.cookies?.firstOrNull { it.name == COOKIE_NAME }?.value ?: return null
        val decoded = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8)
        return decoded.takeIf(::isSafeTarget)
    }

    fun clear(response: HttpServletResponse) {
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            ResponseCookie.from(COOKIE_NAME, "")
                .path("/")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .build()
                .toString(),
        )
    }

    private fun buildTarget(request: HttpServletRequest): String {
        val query = request.queryString?.let { "?$it" } ?: ""
        return request.requestURI + query
    }

    private fun buildCookie(target: String): ResponseCookie {
        return ResponseCookie.from(COOKIE_NAME, URLEncoder.encode(target, StandardCharsets.UTF_8))
            .path("/")
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Lax")
            .maxAge(Duration.ofMinutes(10))
            .build()
    }

    private fun isSafeTarget(target: String): Boolean {
        return target.startsWith("/") &&
            !target.startsWith("//") &&
            !target.startsWith("/login") &&
            !target.startsWith("/logout")
    }

    companion object {
        private const val COOKIE_NAME = "login_redirect"
    }
}
