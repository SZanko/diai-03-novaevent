package pt.unl.fct.iadi.novaevents.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.mock.env.MockEnvironment

class TestJwtService {
    @Test
    fun `generated token preserves subject and roles`() {
        val service = JwtService(
            MockEnvironment()
                .withProperty("app.security.jwt.secret", "NovaEventsJwtSecretKeyForSigningTokens2026NovaEvents")
                .withProperty("app.security.jwt.expiration-seconds", "120"),
        )

        val token = service.generateToken("alice", listOf("ROLE_EDITOR"))
        val claims = service.parseToken(token)

        assertEquals("alice", claims.subject)
        assertEquals(listOf("ROLE_EDITOR"), claims["roles"])
    }
}

