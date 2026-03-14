package pt.unl.fct.iadi.novaevents.controller

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
    fun listAllEvents(model: Model, @RequestParam type: String?, @RequestParam club:String?, @RequestParam from: String?, @RequestParam to: String?) : String {

        model.addAttribute("events", eventsService.getEvents(type=type, club=club, from=from, to=to))

        return "events"
    }

}