package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventsService

@Controller
@RequestMapping("/clubs")
class ClubsController(
    val clubService: ClubService,
    val eventService: EventsService,
) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping
    fun listClubs(model: Model): String {
        log.info("Get all clubs page")

        model.addAttribute("clubs", clubService.findAll())

        return "clubs"
    }

    @GetMapping("/{id}")
    fun getClubDetails(@PathVariable id: Long, model: Model): String {
        log.info("Get club $id details")

        val found = clubService.findClubById(id)

        model.addAttribute("club", found)

        return "club";
    }

    @PostMapping("/{club}/add")
    fun createEvent(
        @PathVariable club: Long,
        @Valid @ModelAttribute("event") event: EventDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clubId", club)
            return "event-add"
        }

        event.clubId = club
        val saved = eventService.saveEvent(event)
        return "redirect:/clubs/${saved.clubId}/${saved.id}"
    }


    @GetMapping("{club}/add")
    fun addEvent(model: Model, @PathVariable club: Long): String {
        log.info("Send Form for adding Event")
        model.addAttribute("event", EventDto())
        model.addAttribute("clubId", club)

        return "event-add"
    }


    @GetMapping("/{club}/{eventId}")
    fun getEvent(@PathVariable club: Long, @PathVariable eventId: Long, model: Model): String {
        log.info("Get club $club Event $eventId")

        val event: EventDto =  eventService.getEventByClubIdAndId(club, eventId)

        model.addAttribute("event", event)

        return "event"
    }

    @GetMapping("/{club}/{eventId}/edit")
    fun editEvent(@PathVariable club: Long, @PathVariable eventId: Long, model: Model): String {
        log.info("Edit club $club Event $eventId")
        val event: EventDto =  eventService.getEventByClubIdAndId(club, eventId)
        model.addAttribute("event", event)
        model.addAttribute("clubId", club)

        return "event"
    }

    @GetMapping("/{club}/{eventId}/delete")
    fun confirmDelete(
        @PathVariable club: Long,
        @PathVariable eventId: Long,
        model: Model
    ): String {
        val event = eventService.getEventByClubIdAndId(club, eventId)
        model.addAttribute("event", event)
        return "event-delete"
    }

    @PostMapping("/{club}/{eventId}/delete")
    fun deleteEvent(
        @PathVariable club: Long,
        @PathVariable eventId: Long
    ): String {
        eventService.deleteEvent(club, eventId)
        return "redirect:/clubs/$club"
    }
}