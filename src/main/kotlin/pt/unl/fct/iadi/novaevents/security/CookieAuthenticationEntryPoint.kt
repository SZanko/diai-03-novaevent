package pt.unl.fct.iadi.novaevents.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CookieAuthenticationEntryPoint(
    private val loginRedirectCookieService: LoginRedirectCookieService,
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        loginRedirectCookieService.saveTarget(request, response)
        response.sendRedirect("/login")
    }
}
