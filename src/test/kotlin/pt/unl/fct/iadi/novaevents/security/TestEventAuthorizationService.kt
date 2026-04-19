package pt.unl.fct.iadi.novaevents.security

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import pt.unl.fct.iadi.novaevents.repositories.EventRepository

class TestEventAuthorizationService {
    private val eventRepository = mock(EventRepository::class.java)
    private val service = EventAuthorizationService(eventRepository)

    @Test
    fun `owner can edit event`() {
        `when`(eventRepository.existsByIdAndOwnerUsername(10, "bob")).thenReturn(true)
        val authentication = UsernamePasswordAuthenticationToken("bob", "token")

        assertTrue(service.isOwner(10, authentication))
    }

    @Test
    fun `missing authentication is not owner`() {
        assertFalse(service.isOwner(10, null))
    }

    @Test
    fun `admin can delete without ownership lookup`() {
        val authentication = UsernamePasswordAuthenticationToken(
            "charlie",
            "token",
            listOf(SimpleGrantedAuthority("ROLE_ADMIN")),
        )

        assertTrue(service.canDelete(10, authentication))
        verify(eventRepository, never()).existsByIdAndOwnerUsername(10, "charlie")
    }
}

