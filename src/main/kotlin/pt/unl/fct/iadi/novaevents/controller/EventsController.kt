package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    @GetMapping("/events")
    fun listAllEvents(
        model: Model,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) club: String?,
        @RequestParam(required = false) from: String?,
        @RequestParam(required = false) to: String?,
    ): String {
        model.addAttribute("events", eventService.getEvents(type = type, club = club, from = from, to = to))
        return "events"
    }

    @GetMapping("/clubs/{clubId}/events/{eventId}")
    fun eventDetail(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String {
        model.addAttribute("event", eventService.getEventByClubIdAndId(clubId, eventId))
        model.addAttribute("club", clubService.findById(clubId))
        return "event"
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @GetMapping("/clubs/{clubId}/events/new")
    fun showCreateForm(@PathVariable clubId: Long, model: Model): String {
        model.addAttribute("club", clubService.findById(clubId))
        model.addAttribute("event", EventDto())
        model.addAttribute("isEdit", false)
        return "event-add"
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
    @PostMapping("/clubs/{clubId}/events")
    fun createEvent(
        @PathVariable clubId: Long,
        @Valid @ModelAttribute("event") event: EventDto,
        bindingResult: BindingResult,
        model: Model,
        authentication: Authentication,
    ): String {
        val club = clubService.findById(clubId)
        if (bindingResult.hasErrors()) {
            model.addAttribute("club", club)
            model.addAttribute("isEdit", false)
            return "event-add"
        }

        event.clubId = club.id

        return try {
            val savedEvent = eventService.saveEvent(event, authentication.name)
            "redirect:/clubs/$clubId/events/${savedEvent.id}"
        } catch (exception: EventDuplicateNameException) {
            bindingResult.rejectValue("name", "duplicate", exception.message ?: "An event with this name already exists")
            model.addAttribute("club", club)
            model.addAttribute("isEdit", false)
            "event-add"
        }
    }

    @PreAuthorize("@eventAuthorization.isOwner(#eventId, authentication)")
    @GetMapping("/clubs/{clubId}/events/{eventId}/edit")
    fun showEditForm(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String {
        model.addAttribute("club", clubService.findById(clubId))
        model.addAttribute("event", eventService.getEventByClubIdAndId(clubId, eventId))
        model.addAttribute("isEdit", true)
        return "event-edit"
    }

    @PreAuthorize("@eventAuthorization.isOwner(#eventId, authentication)")
    @PutMapping("/clubs/{clubId}/events/{eventId}")
    fun updateEvent(
        @PathVariable clubId: Long,
        @PathVariable eventId: Long,
        @Valid @ModelAttribute("event") eventForm: EventDto,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        val club = clubService.findById(clubId)

        if (bindingResult.hasErrors()) {
            eventForm.id = eventId
            eventForm.clubId = club.id
            eventForm.club = club.name
            model.addAttribute("club", club)
            model.addAttribute("event", eventForm)
            model.addAttribute("isEdit", true)
            return "event-edit"
        }

        eventForm.clubId = club.id
        eventForm.id = eventId

        return try {
            eventService.updateEvent(eventId, eventForm)
            "redirect:/clubs/$clubId/events/$eventId"
        } catch (exception: EventDuplicateNameException) {
            bindingResult.rejectValue("name", "duplicate", exception.message ?: "An event with this name already exists")
            eventForm.club = club.name
            model.addAttribute("club", club)
            model.addAttribute("event", eventForm)
            model.addAttribute("isEdit", true)
            "event-edit"
        }
    }

    @PreAuthorize("@eventAuthorization.canDelete(#eventId, authentication)")
    @GetMapping("/clubs/{clubId}/events/{eventId}/delete")
    fun showDeleteConfirmation(@PathVariable clubId: Long, @PathVariable eventId: Long, model: Model): String {
        model.addAttribute("club", clubService.findById(clubId))
        model.addAttribute("event", eventService.getEventByClubIdAndId(clubId, eventId))
        return "event-delete"
    }

    @PreAuthorize("@eventAuthorization.canDelete(#eventId, authentication)")
    @DeleteMapping("/clubs/{clubId}/events/{eventId}")
    fun deleteEvent(@PathVariable clubId: Long, @PathVariable eventId: Long): String {
        eventService.deleteEvent(clubId, eventId)
        return "redirect:/clubs/$clubId"
    }
}
