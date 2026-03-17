package pt.unl.fct.iadi.novaevents.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
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

    @GetMapping("/{club}/{eventId}")
    fun getEvent(@PathVariable club: Long, @PathVariable eventId: Long, model: Model): String {
        log.info("Get club $club/$eventId")

        val event: EventDto =  eventService.getEventByClubIdAndId(club, eventId)

        model.addAttribute("event", event)

        return "event"
    }

    @GetMapping("/{club}/{eventId}/edit")
    fun editEvent(@PathVariable club: Long, @PathVariable eventId: Long, model: Model): String {
        log.info("Edit club $club/$eventId")
        val event: EventDto =  eventService.getEventByClubIdAndId(club, eventId)

        model.addAttribute("event", event)

        return "event"
    }
}