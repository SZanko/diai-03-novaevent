package pt.unl.fct.iadi.novaevents.security

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.repositories.EventRepository

@Service("eventAuthorization")
class EventAuthorizationService(
    private val eventRepository: EventRepository,
) {
    fun isOwner(eventId: Long, authentication: Authentication?): Boolean {
        val username = authentication?.name ?: return false
        return eventRepository.existsByIdAndOwnerUsername(eventId, username)
    }

    fun canDelete(eventId: Long, authentication: Authentication?): Boolean {
        val username = authentication?.name ?: return false
        val authorities = authentication.authorities.map { it.authority }.toSet()
        return authorities.contains("ROLE_ADMIN") || eventRepository.existsByIdAndOwnerUsername(eventId, username)
    }
}
