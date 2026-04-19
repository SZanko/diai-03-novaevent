package pt.unl.fct.iadi.novaevents.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.model.RoleName
import pt.unl.fct.iadi.novaevents.repositories.AppUserRepository
import java.util.Optional

class TestDatabaseUserDetailsManager {
    private val appUserRepository = mock(AppUserRepository::class.java)
    private val passwordEncoder = BCryptPasswordEncoder()
    private val service = DatabaseUserDetailsManager(appUserRepository, passwordEncoder)

    @Test
    fun `createUser encodes password and stores roles`() {
        `when`(appUserRepository.existsByUsername("dora")).thenReturn(false)
        val user = User.withUsername("dora")
            .password("secret")
            .authorities("ROLE_EDITOR")
            .build()

        service.createUser(user)

        val captor = ArgumentCaptor.forClass(AppUser::class.java)
        verify(appUserRepository).save(captor.capture())
        assertEquals("dora", captor.value.username)
        assertNotEquals("secret", captor.value.password)
        assertEquals(RoleName.ROLE_EDITOR, captor.value.roles.single().role)
    }

    @Test
    fun `loadUserByUsername returns spring security user`() {
        val user = AppUser(id = 3, username = "alice", password = "encoded")
        user.replaceRoles(listOf(RoleName.ROLE_ADMIN))
        `when`(appUserRepository.findByUsername("alice")).thenReturn(Optional.of(user))

        val result = service.loadUserByUsername("alice")

        assertEquals("alice", result.username)
        assertEquals("ROLE_ADMIN", result.authorities.single().authority)
    }

    @Test
    fun `missing user throws UsernameNotFoundException`() {
        `when`(appUserRepository.findByUsername("missing")).thenReturn(Optional.empty())

        assertThrows(UsernameNotFoundException::class.java) {
            service.loadUserByUsername("missing")
        }
    }
}

