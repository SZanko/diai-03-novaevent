package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventsService

@Controller
@RequestMapping("/clubs")
class ClubsController(
    private val clubService: ClubService,
    private val eventService: EventsService,
) {
    @GetMapping
    fun listClubs(model: Model): String {
        model.addAttribute("clubs", clubService.findAll())
        return "clubs"
    }

    @GetMapping("/{clubId}")
    fun clubDetail(@PathVariable clubId: Long, model: Model): String {
        model.addAttribute("club", clubService.findById(clubId))
        model.addAttribute("events", eventService.findByClub(clubId))
        return "club"
    }
}
