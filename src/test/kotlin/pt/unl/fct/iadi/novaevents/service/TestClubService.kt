package pt.unl.fct.iadi.novaevents.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.service.exceptions.ClubNotFoundException
import java.util.Optional

class TestClubService {
    private val clubRepository = mock(ClubRepository::class.java)
    private val service = ClubService(clubRepository, ClubConverter())

    @Test
    fun `findAll returns converted clubs`() {
        `when`(clubRepository.findAllWithEvents()).thenReturn(
            listOf(Club(id = 1, name = "Robotics Club", description = "Robots", category = ClubCategorie.TECHNOLOGY)),
        )

        val clubs = service.findAll()

        assertEquals(1, clubs.size)
        assertEquals("Robotics Club", clubs[0].name)
        assertEquals("TECHNOLOGY", clubs[0].category)
    }

    @Test
    fun `findById throws when club is missing`() {
        `when`(clubRepository.findById(99)).thenReturn(Optional.empty())

        assertThrows(ClubNotFoundException::class.java) {
            service.findById(99)
        }
    }
}
