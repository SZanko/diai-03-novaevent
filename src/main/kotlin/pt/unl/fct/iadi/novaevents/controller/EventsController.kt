package pt.unl.fct.iadi.novaevents.controller

import jakarta.websocket.server.PathParam
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.service.EventsService

@Controller
@RequestMapping("/events")
class EventsController(
    private val eventsService: EventsService,

) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping
    fun listAllEvents(
        model: Model,
        @RequestParam type: String?,
        @RequestParam club: String?,
        @RequestParam from: String?,
        @RequestParam to: String?
    ): String {

        model.addAttribute("events", eventsService.getEvents(type = type, club = club, from = from, to = to))

        return "events"
    }


    @GetMapping("/{eventId}")
    fun getEvent(@PathVariable eventId: Long, model: Model): String {
        log.info("Get Event $eventId")

        val event: EventDto = eventsService.getEventById(eventId)

        model.addAttribute("event", event)

        return "event"
    }

    @GetMapping("/{club}/{eventId}")
    fun getEventWithClub(@PathVariable club: Long, @PathVariable eventId: Long, model: Model): String {
        log.info("Get club $club Event $eventId")

        val event: EventDto = eventsService.getEventByClubIdAndId(club, eventId)

        model.addAttribute("event", event)

        return "event"
    }

}