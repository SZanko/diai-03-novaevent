package pt.unl.fct.iadi.novaevents.service

import jakarta.validation.Validation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.controller.dto.EventTypeDTO
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.RoleName
import pt.unl.fct.iadi.novaevents.repositories.AppUserRepository
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.repositories.EventRepository
import pt.unl.fct.iadi.novaevents.service.exceptions.EventDuplicateNameException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventOutdoorRuleException
import java.time.LocalDate
import java.util.Optional

class TestEventsService {
    private val clubRepository = mock(ClubRepository::class.java)
    private val eventRepository = mock(EventRepository::class.java)
    private val appUserRepository = mock(AppUserRepository::class.java)
    private val weatherService = mock(WeatherService::class.java)
    private val validator = Validation.buildDefaultValidatorFactory().validator
    private val service = EventsService(validator, clubRepository, eventRepository, appUserRepository, weatherService)

    @Test
    fun `saves normal club event without weather check`() {
        val club = Club(id = 1, name = "Robotics Club", description = "Robots", category = ClubCategorie.TECHNOLOGY)
        arrangeSave(club)

        val saved = service.saveEvent(eventDto(clubId = 1, location = null), "bob")

        assertEquals("Arduino Day", saved.name)
        verify(weatherService, never()).isRaining(anyString())
    }

    @Test
    fun `hiking club requires location`() {
        val club = Club(id = 4, name = "Hiking & Outdoors Club", description = "Trails", category = ClubCategorie.SPORTS)
        `when`(clubRepository.findById(4)).thenReturn(Optional.of(club))

        val exception = assertThrows(EventOutdoorRuleException::class.java) {
            service.saveEvent(eventDto(clubId = 4, location = " "), "bob")
        }

        assertEquals("location", exception.field)
        assertEquals("Location is required for outdoor events", exception.message)
        verify(eventRepository, never()).save(any(Event::class.java))
    }

    @Test
    fun `hiking club rejects rainy weather`() {
        val club = Club(id = 4, name = "Hiking & Outdoors Club", description = "Trails", category = ClubCategorie.SPORTS)
        `when`(clubRepository.findById(4)).thenReturn(Optional.of(club))
        `when`(weatherService.isRaining("Sintra")).thenReturn(true)

        val exception = assertThrows(EventOutdoorRuleException::class.java) {
            service.saveEvent(eventDto(clubId = 4, location = " Sintra "), "bob")
        }

        assertEquals("It is currently raining at \"Sintra\" — outdoor events cannot be created in bad weather", exception.message)
        verify(eventRepository, never()).save(any(Event::class.java))
    }

    @Test
    fun `hiking club saves when weather is clear`() {
        val club = Club(id = 4, name = "Hiking & Outdoors Club", description = "Trails", category = ClubCategorie.SPORTS)
        arrangeSave(club)
        `when`(weatherService.isRaining("Sintra")).thenReturn(false)

        val saved = service.saveEvent(eventDto(clubId = 4, location = " Sintra "), "bob")

        assertEquals("Sintra", saved.location)
    }

    @Test
    fun `duplicate event name is rejected`() {
        val club = Club(id = 1, name = "Robotics Club", description = "Robots", category = ClubCategorie.TECHNOLOGY)
        `when`(clubRepository.findById(1)).thenReturn(Optional.of(club))
        `when`(eventRepository.existsByNameIgnoreCase("Arduino Day")).thenReturn(true)

        assertThrows(EventDuplicateNameException::class.java) {
            service.saveEvent(eventDto(clubId = 1, location = "Lab"), "bob")
        }
    }

    private fun arrangeSave(club: Club) {
        val user = AppUser(id = 7, username = "bob", password = "encoded")
        user.replaceRoles(listOf(RoleName.ROLE_EDITOR))
        `when`(clubRepository.findById(club.id!!)).thenReturn(Optional.of(club))
        `when`(eventRepository.existsByNameIgnoreCase("Arduino Day")).thenReturn(false)
        `when`(appUserRepository.findByUsername("bob")).thenReturn(Optional.of(user))
        `when`(eventRepository.save(any(Event::class.java))).thenAnswer { invocation ->
            invocation.getArgument<Event>(0).copy(id = 99)
        }
    }

    private fun eventDto(clubId: Long, location: String?): EventDto {
        return EventDto(
            clubId = clubId,
            name = "Arduino Day",
            date = LocalDate.of(2026, 6, 1),
            location = location,
            type = EventTypeDTO.WORKSHOP,
            description = "Build things",
        )
    }
}
