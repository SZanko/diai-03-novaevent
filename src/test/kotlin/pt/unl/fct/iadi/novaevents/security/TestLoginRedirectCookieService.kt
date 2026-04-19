package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.mock.env.MockEnvironment
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class TestLoginRedirectCookieService {
    private val service = LoginRedirectCookieService(MockEnvironment().withProperty("app.security.jwt.cookie-secure", "false"))

    @Test
    fun `saveTarget stores safe get target`() {
        val request = MockHttpServletRequest("GET", "/clubs/4")
        request.setQueryString("from=2026-01-01")
        val response = MockHttpServletResponse()

        service.saveTarget(request, response)

        assertTrue((response.getHeader(HttpHeaders.SET_COOKIE) ?: "").contains("login_redirect="))
    }

    @Test
    fun `resolveTarget decodes safe cookie`() {
        val request = MockHttpServletRequest()
        request.setCookies(Cookie("login_redirect", URLEncoder.encode("/clubs/4", StandardCharsets.UTF_8)))

        assertEquals("/clubs/4", service.resolveTarget(request))
    }

    @Test
    fun `unsafe target is ignored`() {
        val request = MockHttpServletRequest()
        request.setCookies(Cookie("login_redirect", URLEncoder.encode("//evil.example", StandardCharsets.UTF_8)))

        assertNull(service.resolveTarget(request))
    }
}
