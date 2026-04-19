package pt.unl.fct.iadi.novaevents.controller

import jakarta.servlet.http.Cookie
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.repositories.EventRepository
import pt.unl.fct.iadi.novaevents.security.JwtService
import pt.unl.fct.iadi.novaevents.service.WeatherService

@SpringBootTest
@AutoConfigureMockMvc
class TestNovaEventsEndpoints {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var clubRepository: ClubRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @MockBean
    private lateinit var weatherService: WeatherService

    @Test
    fun `public pages render`() {
        val club = clubRepository.findAll().first()
        val event = eventRepository.findEventsByClubId(club.id!!).first()

        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/clubs"))

        mockMvc.perform(get("/clubs"))
            .andExpect(status().isOk)
            .andExpect(view().name("clubs"))

        mockMvc.perform(get("/clubs/${club.id}"))
            .andExpect(status().isOk)
            .andExpect(view().name("club"))

        mockMvc.perform(get("/events"))
            .andExpect(status().isOk)
            .andExpect(view().name("events"))

        mockMvc.perform(get("/clubs/${club.id}/events/${event.id}"))
            .andExpect(status().isOk)
            .andExpect(view().name("event"))
    }

    @Test
    fun `login endpoint issues jwt cookie`() {
        mockMvc.perform(post("/login").param("username", "alice").param("password", "password123"))
            .andExpect(status().is3xxRedirection)
            .andExpect(cookie().exists(JwtService.JWT_COOKIE_NAME))
    }

    @Test
    fun `weather api requires authentication`() {
        mockMvc.perform(get("/api/weather").param("location", "Lisbon").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `weather api returns json when requested`() {
        `when`(weatherService.isRaining("Lisbon")).thenReturn(false)

        mockMvc.perform(
            get("/api/weather")
                .param("location", "Lisbon")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(editorCookie()),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.raining").value(false))
    }

    @Test
    fun `weather api returns html fragment when requested`() {
        `when`(weatherService.isRaining("Lisbon")).thenReturn(false)

        mockMvc.perform(
            get("/api/weather")
                .param("location", "Lisbon")
                .accept(MediaType.TEXT_HTML)
                .cookie(editorCookie()),
        )
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Clear")))
            .andExpect(content().string(containsString("badge bg-success")))
    }

    @Test
    fun `hiking event creation requires a location`() {
        val club = hikingClub()
        val eventName = "No Location Trail"

        mockMvc.perform(
            post("/clubs/${club.id}/events")
                .cookie(editorCookie())
                .with(csrf())
                .param("name", eventName)
                .param("date", "2026-07-01")
                .param("type", "SOCIAL")
                .param("location", " "),
        )
            .andExpect(status().isOk)
            .andExpect(view().name("event-add"))
            .andExpect(model().attributeHasFieldErrors("event", "location"))
            .andExpect(content().string(containsString("Location is required for outdoor events")))

        assertFalse(eventRepository.existsByNameIgnoreCase(eventName))
    }

    @Test
    fun `hiking event creation rejects rainy weather`() {
        val club = hikingClub()
        val eventName = "Rainy Trail"
        `when`(weatherService.isRaining("Sintra")).thenReturn(true)

        mockMvc.perform(
            post("/clubs/${club.id}/events")
                .cookie(editorCookie())
                .with(csrf())
                .param("name", eventName)
                .param("date", "2026-07-02")
                .param("type", "SOCIAL")
                .param("location", "Sintra"),
        )
            .andExpect(status().isOk)
            .andExpect(view().name("event-add"))
            .andExpect(model().attributeHasFieldErrors("event", "location"))
            .andExpect(content().string(containsString("It is currently raining at &quot;Sintra&quot;")))

        assertFalse(eventRepository.existsByNameIgnoreCase(eventName))
    }

    @Test
    fun `hiking event creation succeeds in clear weather`() {
        val club = hikingClub()
        val eventName = "Sunny Trail"
        `when`(weatherService.isRaining("Sintra")).thenReturn(false)

        mockMvc.perform(
            post("/clubs/${club.id}/events")
                .cookie(editorCookie())
                .with(csrf())
                .param("name", eventName)
                .param("date", "2026-07-03")
                .param("type", "SOCIAL")
                .param("location", "Sintra"),
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrlPattern("/clubs/${club.id}/events/*"))

        assertTrue(eventRepository.existsByNameIgnoreCase(eventName))
    }

    @Test
    fun `authenticated editor can open create form`() {
        val club = hikingClub()

        mockMvc.perform(get("/clubs/${club.id}/events/new").cookie(editorCookie()))
            .andExpect(status().isOk)
            .andExpect(view().name("event-add"))
            .andExpect(content().string(containsString("Check Weather JS")))
            .andExpect(content().string(containsString("Check Weather HTMX")))
    }

    private fun editorCookie(): Cookie {
        return Cookie(JwtService.JWT_COOKIE_NAME, jwtService.generateToken("alice", listOf("ROLE_EDITOR")))
    }

    private fun hikingClub() = clubRepository.findAll().first { it.name == "Hiking & Outdoors Club" }
}
