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
class ClubController(
    private val clubService: ClubService,
    private val eventService: EventsService
) {

    @GetMapping
    fun listClubs(model: Model): String {
        model.addAttribute("clubs", clubService.findAll())
        return "clubs"
    }

    @GetMapping("/{clubId}")
    fun clubDetail(@PathVariable clubId: Long, model: Model): String {
        val club = clubService.findById(clubId)
        model.addAttribute("club", club)
        model.addAttribute("events", eventService.findByClub(clubId))
        return "club"
    }
}