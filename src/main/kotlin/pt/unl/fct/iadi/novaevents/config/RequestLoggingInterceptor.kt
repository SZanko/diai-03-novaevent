package pt.unl.fct.iadi.novaevents.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RequestLoggingInterceptor : HandlerInterceptor {
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = when {
            authentication == null -> "anonymous"
            authentication is AnonymousAuthenticationToken -> "anonymous"
            !authentication.isAuthenticated -> "anonymous"
            else -> authentication.name
        }
        val uri = request.requestURI + (request.queryString?.let { "?$it" } ?: "")
        log.info("[{}] {} {} [{}]", principal, request.method, uri, response.status)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RequestLoggingInterceptor::class.java)
    }
}
