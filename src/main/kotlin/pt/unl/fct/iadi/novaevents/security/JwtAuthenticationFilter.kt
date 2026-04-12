package pt.unl.fct.iadi.novaevents.security

import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = request.cookies?.firstOrNull { it.name == JwtService.JWT_COOKIE_NAME }?.value

        if (!token.isNullOrBlank() && SecurityContextHolder.getContext().authentication == null) {
            try {
                val claims = jwtService.parseToken(token)
                val roles = (claims["roles"] as? Collection<*>)
                    .orEmpty()
                    .filterIsInstance<String>()
                    .map(::SimpleGrantedAuthority)
                val authentication = UsernamePasswordAuthenticationToken.authenticated(
                    claims.subject,
                    token,
                    roles,
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (_: JwtException) {
                jwtService.clearJwtCookie(response)
            } catch (_: IllegalArgumentException) {
                jwtService.clearJwtCookie(response)
            }
        }

        filterChain.doFilter(request, response)
    }
}
