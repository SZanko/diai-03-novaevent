package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventsService
import pt.unl.fct.iadi.novaevents.service.exceptions.EventDuplicateNameException

@Controller
class EventsController(
    private val eventService: EventsService,
    private val clubService: ClubService,

    ) {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/events")
    fun listAllEvents(
        model: Model,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) club: String?,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) to: String?
    ): String {

        log.info("Called events route")

        val events = eventService.getEvents(type = type, club = club, from = from, to = to)
        log.info("Found ${events.size} events")

        model.addAttribute("events", events)

        return "events"
    }

    @GetMapping("/clubs/{clubId}/events/{eventId}")
    fun eventDetail(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String {
        val event = eventService.getEventByClubIdAndId(clubId, eventId)
        model.addAttribute("event", event)
        model.addAttribute("club", clubService.findById(clubId))
        return "event"
    }



    @GetMapping("/clubs/{clubId}/events/new")
    fun showCreateForm(@PathVariable clubId: Long, model: Model): String {
        val club = clubService.findById(clubId)
        model.addAttribute("club", club)
        model.addAttribute("event", EventDto())
        model.addAttribute("isEdit", false)
        return "event-add"
    }

    @PostMapping("/clubs/{clubId}/events")
    fun createEvent(
        @PathVariable clubId: Long,
        @Valid @ModelAttribute("event") event: EventDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        val club = clubService.findById(clubId)
        if (bindingResult.hasErrors()) {
            model.addAttribute("club", club)
            model.addAttribute("isEdit", false)
            return "event-add"
        }
        
        event.clubId = club.id;

        return try {
            val event = eventService.saveEvent(event)
            "redirect:/clubs/$clubId/events/${event.id}"
        } catch (exception: EventDuplicateNameException) {
            bindingResult.rejectValue("name", "duplicate", exception.message ?: "An event with this name already exists")
            model.addAttribute("club", club)
            model.addAttribute("isEdit", false)
            "event-add"
        }
    }





    @GetMapping("/clubs/{clubId}/events/{eventId}/edit")
    fun showEditForm(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String {
        log.info("Called events edit route")
        val club = clubService.findById(clubId)
        val event = eventService.getEventByClubIdAndId(clubId, eventId)
        model.addAttribute("club", club)
        model.addAttribute("event", event)
        model.addAttribute("isEdit", true)
        return "event-edit"
    }

    @PutMapping("/clubs/{clubId}/events/{eventId}")
    // Test assumes PUT not post
    //@PostMapping("/clubs/{clubId}/events/{eventId}")
    fun updateEvent(
        @PathVariable clubId: Long,
        @PathVariable eventId: Long,
        @Valid @ModelAttribute("event") eventForm: EventDto,
        bindingResult: BindingResult,
        model: Model
    ): String {
        val club = clubService.findById(clubId)
        val event = eventService.getEventByClubIdAndId(clubId, eventId)

        if (bindingResult.hasErrors()) {
            model.addAttribute("club", club)
            model.addAttribute("event", event)
            model.addAttribute("isEdit", true)
            return "event-edit"
        }

        eventForm.clubId = club.id;
        eventForm.id = eventId;

        return try {
            eventService.updateEvent(eventId, eventForm)
            "redirect:/clubs/$clubId/events/$eventId"
        } catch (exception: EventDuplicateNameException) {
            bindingResult.rejectValue("name", "duplicate", exception.message ?: "An event with this name already exists")
            model.addAttribute("club", club)
            model.addAttribute("event", eventForm)
            model.addAttribute("isEdit", true)
            "event-edit"
        }
    }





    @GetMapping("/clubs/{clubId}/events/{eventId}/delete")
    fun showDeleteConfirmation(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String {
        model.addAttribute("club", clubService.findById(clubId))
        model.addAttribute("event", eventService.getEventByClubIdAndId(clubId, eventId))
        return "event-delete"
    }


    @DeleteMapping("/clubs/{clubId}/events/{eventId}")
    fun deleteEvent(@PathVariable clubId: Long, @PathVariable eventId: Long): String {
        eventService.deleteEvent(eventId)
        return "redirect:/clubs/$clubId"
    }

}