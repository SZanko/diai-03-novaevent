package pt.unl.fct.iadi.novaevents.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.security.JwtService
import pt.unl.fct.iadi.novaevents.security.LoginRedirectCookieService

@Controller
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val loginRedirectCookieService: LoginRedirectCookieService,
) {
    @GetMapping("/login")
    fun loginPage(
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) logout: String?,
        authentication: Authentication?,
        request: HttpServletRequest,
        response: HttpServletResponse,
        model: Model,
    ): String {
        if (authentication != null && authentication !is AnonymousAuthenticationToken && authentication.isAuthenticated) {
            val target = loginRedirectCookieService.resolveTarget(request) ?: "/clubs"
            loginRedirectCookieService.clear(response)
            return "redirect:$target"
        }

        model.addAttribute("hasError", error != null)
        model.addAttribute("loggedOut", logout != null)
        return "login"
    }

    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): String {
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, password),
            )
            val token = jwtService.generateToken(
                authentication.name,
                authentication.authorities.map { it.authority },
            )
            jwtService.addJwtCookie(response, token)
            val target = loginRedirectCookieService.resolveTarget(request) ?: "/clubs"
            loginRedirectCookieService.clear(response)
            "redirect:$target"
        } catch (_: AuthenticationException) {
            "redirect:/login?error"
        }
    }
}
